package ru.researchser.DAO;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.researchser.DAO.interfaces.RoleDao;
import ru.researchser.models.Role;
import ru.researchser.models.enums.ERole;

import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class RoleJdbcTemplate implements RoleDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Role> findByName(ERole name) {
        return Optional.empty();
    }

    @Override
    public Set<Role> findAll() {
        return null;
    }

    @Override
    public Set<Role> findAllByUserId(Long id) {
        return null;
    }

    @Override
    public Role save(Role role) {
        return null;
    }

    @Override
    public Role update(Role role) {
        return null;
    }

    @Override
    public Role updateById(Long id, Role role) {
        return null;
    }

    @Override
    public Set<Role> updateAll(Set<Role> roles) {
        return null;
    }

    @Override
    public int deleteById(Long id) {
        return 0;
    }

    @Override
    public int delete(Role role) {
        return 0;
    }

    @Override
    public int deleteAll() {
        return 0;
    }
}
