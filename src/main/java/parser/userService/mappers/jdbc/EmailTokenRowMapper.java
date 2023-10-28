package parser.userService.mappers.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import parser.userService.DAO.interfaces.UserDao;
import parser.userService.exceptions.NotFoundException;
import parser.userService.models.EmailToken;
import parser.userService.models.User;
import parser.userService.models.enums.TokenType;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class EmailTokenRowMapper implements RowMapper<EmailToken> {

    private final UserDao userDao;

    @Override
    public EmailToken mapRow(ResultSet rs, int rowNum) throws SQLException {
        return EmailToken
                .builder()
                .id(rs.getLong("id"))
                .token(rs.getString("token"))
                .expirationDate(rs.getTimestamp("expiration_date"))
                .user(findUserById(rs.getLong("user_id")))
                .tokenType(TokenType.fromValue(rs.getString("token_type")))
                .build();
    }

    private User findUserById(Long id) {
        return userDao
                .findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d wasn't found", id)));
    }
}
