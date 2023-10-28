package parser.userService.DAO.interfaces;

import org.springframework.stereotype.Repository;
import parser.userService.models.Role;
import parser.userService.models.enums.ERole;

import java.util.Set;

@Repository
public interface RoleDao {
    Role findByName(ERole name);

    Set<Role> findAll();

    Set<Role> findAllByUserId(Long id);

    Role save(Role role);

    Role update(Role role);

    int delete(Role role);

    int deleteAll();
}
