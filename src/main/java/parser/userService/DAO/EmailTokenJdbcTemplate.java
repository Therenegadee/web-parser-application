package parser.userService.DAO;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import parser.userService.DAO.interfaces.EmailTokenDao;
import parser.userService.exceptions.NotFoundException;
import parser.userService.mappers.jdbc.EmailTokenRowMapper;
import parser.userService.models.EmailToken;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class EmailTokenJdbcTemplate implements EmailTokenDao {
    private final JdbcTemplate jdbcTemplate;
    private final EmailTokenRowMapper tokenMapper;

    @Override
    public Optional<EmailToken> findByToken(String token) {
        String query = "SELECT * FROM email_tokens WHERE token=?";
        return jdbcTemplate
                .query(query, tokenMapper, token)
                .stream()
                .findAny();
    }

    @Override
    public Optional<EmailToken> findById(Long id) {
        String query = "SELECT * FROM email_tokens WHERE id=?";
        return jdbcTemplate
                .query(query, tokenMapper, id)
                .stream()
                .findAny();
    }

    @Override
    public Set<EmailToken> findAll() {
        String query = "SELECT * FROM email_tokens";
        return new HashSet<>(jdbcTemplate.query(query, tokenMapper));
    }

    @Override
    public EmailToken save(EmailToken emailToken) {
        if (Objects.isNull(emailToken)) throw new IllegalArgumentException("Email Token is Null!");
        String query = "INSERT INTO email_tokens (token,expiration_date,user_id,token_type)" +
                " VALUES(?,?,?,?) RETURNING id";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        Date date = new Date(emailToken.getExpirationDate().getTime());

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            ps.setString(index++, emailToken.getToken());
            ps.setDate(index++, date);
            ps.setLong(index++, emailToken.getId());
            ps.setString(index++, emailToken.getTokenType().getValue());
            return ps;
        }, keyHolder);

        return findById(Objects.requireNonNull(keyHolder.getKey()).longValue())
                .orElseThrow(() -> new RuntimeException("Email Token doesn't exist"));
    }

    @Override
    public EmailToken update(EmailToken emailToken) {
        if (Objects.isNull(emailToken)) throw new IllegalArgumentException("Email Token is Null!");
        Long id = emailToken.getId();
        return updateById(id, emailToken);
    }

    @Override
    public EmailToken updateById(Long id, EmailToken emailToken) {
        if (Objects.isNull(emailToken)) throw new IllegalArgumentException("Email Token is Null!");
        if (Objects.nonNull(id) && findById(id).isPresent()) {
            Date date = new Date(emailToken.getExpirationDate().getTime());
            String query = "UPDATE email_tokens SET token=?,expiration_date=?,user_id=?," +
                    "token_type=? WHERE id=?";
            int rows = jdbcTemplate.update(query, emailToken.getToken(), date,
                    emailToken.getUser().getId(), emailToken.getTokenType().getValue(), id);
            if (rows != 1) {
                throw new RuntimeException("Invalid request in SQL: " + query);
            }
            return findById(id).get();
        } else {
            throw new NotFoundException(String.format("EmailToken with id %d wasn't found", id));
        }
    }

    @Override
    public int deleteById(Long id) {
        if (Objects.nonNull(id) && findById(id).isPresent()) {
            String query = "DELETE FROM email_tokens WHERE id=?";
            return jdbcTemplate.update(query, id);
        } else {
            throw new NotFoundException(String.format("EmailToken with id %d wasn't found", id));
        }
    }

    @Override
    public int delete(EmailToken emailToken) {
        if (Objects.isNull(emailToken)) throw new IllegalArgumentException("EmailToken is Null!");
        Long id = emailToken.getId();
        return deleteById(id);
    }


    @Override
    public int deleteAllExpired(Date date) {
        String query = "DELETE FROM email_tokens " +
                "WHERE EXTRACT(EPOCH FROM ( ? - expiration_date)) / 86400 > 2;";
        return jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setTimestamp(1, new Timestamp(date.getTime()));
            return ps;
        });
    }
}
