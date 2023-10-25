package ru.researchser.mappers.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.researchser.DAO.interfaces.UserDao;
import ru.researchser.DAO.interfaces.UserParserSettingsDao;
import ru.researchser.exceptions.NotFoundException;
import ru.researchser.models.ParserResult;
import ru.researchser.models.User;
import ru.researchser.models.UserParserSetting;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class ParserResultRowMapper implements RowMapper<ParserResult> {

    private final UserParserSettingsDao userParserSettingsDao;
    private final UserDao userDao;

    @Override
    public ParserResult mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ParserResult
                .builder()
                .id(rs.getLong("id"))
                .userParserSettings(getParserSettingsById(rs.getLong("user_parser_settings_id")))
                .user(getUserById(rs.getLong("user_id")))
                .linkToDownloadResults(rs.getString("link_to_download"))
                .build();
    }

    private UserParserSetting getParserSettingsById(Long id) {
        return userParserSettingsDao.findById(id)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Parser Settings with id %d wasn't found", id)));
    }

    private User getUserById(Long id) {
        return userDao.findById(id)
                .orElseThrow(
                        () -> new NotFoundException(String.format("User with id %d wasn't found", id)));
    }

}
