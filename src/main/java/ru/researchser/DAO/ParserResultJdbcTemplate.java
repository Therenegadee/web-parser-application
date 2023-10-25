package ru.researchser.DAO;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.researchser.DAO.interfaces.ParserResultDao;
import ru.researchser.exceptions.NotFoundException;
import ru.researchser.mappers.jdbc.ParserResultRowMapper;
import ru.researchser.models.ParserResult;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class ParserResultJdbcTemplate implements ParserResultDao {
    private final JdbcTemplate jdbcTemplate;
    private final ParserResultRowMapper parserResultMapper;

    @Override
    public Optional<ParserResult> findById(Long id) {
        String query = "SELECT * FROM parser_results WHERE id=?";
        return jdbcTemplate
                .query(query, parserResultMapper, id)
                .stream()
                .findAny();
    }

    @Override
    public Optional<ParserResult> findByParserSettingsId(Long id) {
        String query = "SELECT * FROM parser_results WHERE user_parser_settings_id=?";
        return jdbcTemplate
                .query(query, parserResultMapper, id)
                .stream()
                .findAny();
    }

    @Override
    public ParserResult save(ParserResult parserResult) {
        if (Objects.isNull(parserResult)) throw new IllegalArgumentException("Parser Result is Null!");
        String query = "INSERT INTO parser_results (user_parser_settings_id,link_to_download)" +
                " VALUES(?,?) RETURNING id";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            ps.setLong(index++, parserResult.getUserParserSettings().getId());
            ps.setString(index++, parserResult.getLinkToDownloadResults());
            return ps;
        }, keyHolder);

        return findById(Objects.requireNonNull(keyHolder.getKey()).longValue())
                .orElseThrow(() -> new RuntimeException("Parser Result doesn't exist"));
    }

    @Override
    public ParserResult update(ParserResult parserResult) {
        if (Objects.isNull(parserResult)) throw new IllegalArgumentException("Parse rResult is Null!");
        Long id = parserResult.getId();
        return updateById(id, parserResult);
    }

    @Override
    public ParserResult updateById(Long id, ParserResult parserResult) {
        if (Objects.isNull(parserResult)) throw new IllegalArgumentException("Parser Result is Null!");
        if (Objects.nonNull(id) && findById(id).isPresent()) {
            String query = "UPDATE parser_results SET user_parser_settings_id=?,link_to_download=?" +
                    " WHERE id=?";
            int rows = jdbcTemplate.update(query, parserResult.getUserParserSettings().getId(),
                    parserResult.getLinkToDownloadResults(), id);
            if (rows != 1) {
                throw new RuntimeException("Invalid request in SQL: " + query);
            }
            return findById(id).get();
        } else {
            throw new NotFoundException(String.format("Parser Result with id %d wasn't found", id));
        }
    }

    @Override
    public Set<ParserResult> findAll() {
        String query = "SELECT * FROM parser_results";
        return new HashSet<>(jdbcTemplate.query(query, parserResultMapper));
    }

    @Override
    public Set<ParserResult> findAllByUserId(Long id) {
        if (Objects.nonNull(id)) {
            String query = "SELECT * FROM parser_results WHERE user_id=?";
            return new HashSet<>(jdbcTemplate.query(query, parserResultMapper, id));
        } else {
            throw new IllegalStateException("The id is null!");
        }
    }

    @Override
    public Set<ParserResult> findAllByIds(List<Long> ids) {
        if (Objects.nonNull(ids) && !ids.isEmpty()) {
            String query = "SELECT * FROM parser_results WHERE id IN (?)";
            return new HashSet<>(jdbcTemplate.query(query, parserResultMapper, ids));
        } else {
            throw new IllegalStateException("The ids are null!");
        }
    }

    @Override
    public int deleteById(Long id) {
        if (Objects.nonNull(id) && findById(id).isPresent()) {
            String query = "DELETE FROM parser_results WHERE id=?";
            return jdbcTemplate.update(query, id);
        } else {
            throw new NotFoundException(String.format("ParserResult with id %d wasn't found", id));
        }
    }

    @Override
    public int delete(ParserResult parserResult) {
        if (Objects.isNull(parserResult)) throw new IllegalArgumentException("ParserResult is Null!");
        Long id = parserResult.getId();
        return deleteById(id);
    }

    @Override
    public int deleteAll() {
        String query = "DELETE FROM parser_results";
        return jdbcTemplate.update(query);
    }
}
