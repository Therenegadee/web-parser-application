package ru.researchser.DAO;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.researchser.DAO.interfaces.ParserResultDao;
import ru.researchser.mappers.jdbc.ParserResultRowMapper;
import ru.researchser.models.ParserResult;

import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ParserResultJdbcTemplate implements ParserResultDao {
    private final JdbcTemplate jdbcTemplate;
    private final ParserResultRowMapper parserResultMapper;

    @Override
    public Optional<ParserResult> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<ParserResult> findByParserSettingsId(Long id) {
        return Optional.empty();
    }

    @Override
    public ParserResult save(ParserResult parserResult) {
        return null;
    }

    @Override
    public ParserResult update(ParserResult parserResult) {
        return null;
    }

    @Override
    public ParserResult updateById(Long id, ParserResult parserResult) {
        return null;
    }

    @Override
    public Set<ParserResult> findAll() {
        return null;
    }

    @Override
    public Set<ParserResult> findAllByUserId(Long id) {
        return null;
    }

    @Override
    public int deleteById(Long id) {
        return 0;
    }

    @Override
    public int delete(ParserResult parserResult) {
        return 0;
    }

    @Override
    public int deleteAll() {
        return 0;
    }
}
