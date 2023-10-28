package parser.userService.DAO.interfaces;

import org.springframework.stereotype.Repository;
import parser.userService.models.Role;
import parser.userService.models.User;
import parser.userService.models.enums.ERole;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserDao {
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
    Optional<User> findByTelegramId(String telegramId);
    Set<User> findAll();
    User save(User user);

    User update(User user);

    User updateById(Long id, User user);

    int deleteById(Long id);

    int delete(User user);

    int deleteAll();

    Set<Role> addRole(Long userId, ERole roleName);

    Set<Role> addRoles(Long userId, Set<ERole> roleNames);

    // Удалить роль
    Set<Role> removeRole(Long userId, ERole roleName);
    User removeAllRoles(Long userId);

    Set<Role> getRolesByUserId(Long userId);
}
