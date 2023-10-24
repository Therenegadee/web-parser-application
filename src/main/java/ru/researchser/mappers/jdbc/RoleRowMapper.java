package ru.researchser.mappers.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.researchser.models.Role;
import ru.researchser.models.enums.ERole;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RoleRowMapper implements RowMapper<Role> {

    @Override
    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
        Role role = new Role(ERole.fromValue(rs.getString("name")));
        role.setId(rs.getLong("id"));
        return null;
    }
}
