package ru.researchser.DAO;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.researchser.DAO.interfaces.RoleDao;
import ru.researchser.exceptions.BadRequestException;
import ru.researchser.exceptions.NotFoundException;
import ru.researchser.mappers.jdbc.RoleRowMapper;
import ru.researchser.models.Role;
import ru.researchser.models.enums.ERole;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class RoleJdbcTemplate implements RoleDao {
    private final JdbcTemplate jdbcTemplate;
    private final RoleRowMapper roleMapper;

    @Override
    public Role findByName(ERole name) {
        String query = "SELECT * FROM roles WHERE name=?";
        return jdbcTemplate
                .query(query, roleMapper, name.getValue())
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException(String.format("Role with name %s doesn't exist", name.getValue())));
    }

    @Override
    public Set<Role> findAll() {
        String query = "SELECT * FROM roles";
        return new HashSet<>(jdbcTemplate.query(query, roleMapper));
    }

    @Override
    public Set<Role> findAllByUserId(Long id) {
        if (Objects.nonNull(id)) {
            String query = "SELECT r.* FROM roles r " +
                    "JOIN user_roles ur " +
                    "ON ur.role_id=r.id WHERE ur.user_id=?";
            return new HashSet<>(jdbcTemplate.query(query, roleMapper, id));
        } else {
            throw new IllegalStateException("The id is null!");
        }
    }

    @Override
    public Role save(Role role) {
        if (Objects.isNull(role)) throw new IllegalArgumentException("Parser Result is Null!");
        ERole roleName = role.getName();
        if (roleName.equals(findByName(roleName).getName()))
            throw new BadRequestException(String.format("The role with name %s is already present in DB", role.getName().getValue()));

        String query = "INSERT INTO roles (name)" +
                " VALUES(?) RETURNING id";

        jdbcTemplate.update(query, role.getName().getValue());

        return findByName(role.getName());
    }

    @Override
    public Role update(Role role) {
        if (Objects.isNull(role)) throw new IllegalArgumentException("Role is Null!");
        ERole roleName = role.getName();
        if (findByName(roleName) != null) {
            String query = "UPDATE roles SET name=?" +
                    " WHERE name=?";
            int rows = jdbcTemplate.update(query, roleName.getValue(), roleName.getValue());
            if (rows != 1) {
                throw new RuntimeException("Invalid request in SQL: " + query);
            }
            return findByName(roleName);
        } else {
            throw new NotFoundException(String.format("Role Name with name %s wasn't found", role.getName().getValue()));
        }
    }

    @Override
    public int delete(Role role) {
        if (Objects.nonNull(role) && findByName(role.getName()) != null) {
            String query = "DELETE FROM roles WHERE name=?";
            return jdbcTemplate
                    .update(query, role.getName().getValue());
        } else {
            throw new NotFoundException(String.format("Role Name with name %s wasn't found", role.getName().getValue()));
        }
    }

    @Override
    public int deleteAll() {
        String query = "DELETE FROM roles";
        return jdbcTemplate
                .update(query);
    }
}
