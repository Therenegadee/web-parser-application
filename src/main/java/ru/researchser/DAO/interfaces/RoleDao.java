package ru.researchser.DAO.interfaces;

import org.springframework.stereotype.Repository;
import ru.researchser.models.ParserResult;
import ru.researchser.models.Role;
import ru.researchser.models.enums.ERole;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleDao {
    Optional<Role> findByName(ERole name);
    Set<Role> findAll();
    Set<Role> findAllByUserId(Long id);
    Role save(Role role);

    Role update(Role role);

    Role updateById(Long id, Role role);
    Set<Role> updateAll(Set<Role> roles);

    int deleteById(Long id);

    int delete(Role role);

    int deleteAll();
}
