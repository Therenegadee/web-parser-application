package parser.userService.DAO;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import parser.userService.DAO.interfaces.RoleDao;
import parser.userService.DAO.interfaces.UserDao;
import parser.userService.exceptions.NotFoundException;
import parser.userService.mappers.jdbc.RoleRowMapper;
import parser.userService.mappers.jdbc.UserRowMapper;
import parser.userService.models.Role;
import parser.userService.models.User;
import parser.userService.models.enums.ERole;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Observed
@Repository
@RequiredArgsConstructor
public class UserJdbcTemplate implements UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userMapper;
    private final RoleDao roleDao;
    private final RoleRowMapper roleMapper;

    @Transactional
    @Override
    public Optional<User> findById(Long id) {
        String query = "SELECT * FROM users WHERE id=?";
        return jdbcTemplate
                .query(query, userMapper, id)
                .stream()
                .findAny();
    }

    @Transactional
    @Override
    public Optional<User> findByUsername(String username) {
        String query = "SELECT * FROM users WHERE username=?";
        return jdbcTemplate
                .query(query, userMapper, username)
                .stream()
                .findAny();
    }

    @Transactional
    @Override
    public Optional<User> findByEmail(String email) {
        String query = "SELECT * FROM users WHERE email=?";
        return jdbcTemplate
                .query(query, userMapper, email)
                .stream()
                .findAny();
    }

    @Transactional
    @Override
    public Optional<User> findByTelegramId(String telegramId) {
        String query = "SELECT * FROM users WHERE telegram_id=?";
        return jdbcTemplate
                .query(query, userMapper, telegramId)
                .stream()
                .findAny();
    }

    @Transactional
    @Override
    public Set<User> findAll() {
        String query = "SELECT * FROM users";
        return new HashSet<>(jdbcTemplate.query(query, userMapper));
    }

    @Transactional
    @Override
    public User save(User user) {
        if (Objects.isNull(user)) throw new IllegalArgumentException("User is Null!");

        String queryToUserTable = "INSERT INTO users (username,email,password,activation_status,telegram_id)" +
                " VALUES(?,?,?,?,?) RETURNING id";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(queryToUserTable, Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            ps.setString(index++, user.getUsername());
            ps.setString(index++, user.getEmail());
            ps.setString(index++, user.getPassword());
            ps.setString(index++, user.getActivationStatus().getValue());
            ps.setString(index++, user.getTelegramUserId());
            return ps;
        }, keyHolder);

        Long userId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        User savedUser = findById(userId)
                .orElseThrow(() -> new RuntimeException("User doesn't exist"));
        return savedUser;
    }

    @Transactional
    @Override
    public User update(User user) {
        if (Objects.isNull(user)) throw new IllegalArgumentException("User is Null!");
        Long id = user.getId();
        return updateById(id, user);
    }

    @Transactional
    @Override
    public User updateById(Long id, User user) {
        if (Objects.isNull(user)) throw new IllegalArgumentException("User is Null!");
        if (Objects.nonNull(id) && findById(id).isPresent()) {
            String query = "UPDATE users SET username=?,email=?,password=?,activation_status=?,telegram_id=?" +
                    " WHERE id=?";
            int rows = jdbcTemplate.update(query, user.getUsername(), user.getEmail(), user.getPassword(),
                    user.getActivationStatus().getValue(), user.getTelegramUserId(), id);
            if (rows != 1) {
                throw new RuntimeException("Invalid request in SQL: " + query);
            }
            return findById(id).get();
        } else {
            throw new NotFoundException(String.format("User with id %d wasn't found", id));
        }
    }

    @Transactional
    @Override
    public int deleteById(Long id) {
        if (Objects.nonNull(id) && findById(id).isPresent()) {
            String query = "DELETE FROM users WHERE id=?";
            return jdbcTemplate.update(query, id);
        } else {
            throw new NotFoundException(String.format("User with id %d wasn't found", id));
        }
    }

    @Transactional
    @Override
    public int delete(User user) {
        if (Objects.isNull(user)) throw new IllegalArgumentException("User is Null!");
        Long id = user.getId();
        return deleteById(id);
    }

    @Transactional
    @Override
    public int deleteAll() {
        String query = "DELETE FROM users";
        return jdbcTemplate.update(query);
    }

    @Transactional
    @Override
    public Set<Role> addRole(Long userId, ERole roleName) {
        Optional<User> user = findById(userId);
        Role role = roleDao.findByName(roleName);
        if (user.isPresent() && role != null) {
            String query = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
            jdbcTemplate.update(query, userId, role.getId());
            return new HashSet<>(roleDao.findAllByUserId(userId));
        } else {
            throw new NotFoundException(String.format("User (id %d) or Role wasn't found", userId));
        }
    }

    @Transactional
    @Override
    public Set<Role> addRoles(Long userId, Set<ERole> roleNames) {
        Optional<User> user = findById(userId);
        List<Role> roles = new ArrayList<>();
        roleNames.stream()
                .toList()
                .forEach(roleName -> roles.add(roleDao.findByName(roleName)));
        if (user.isPresent() && roles.size() == roleNames.size()) {
            String query = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
            for(Role role : roles) {
                jdbcTemplate.update(query, userId, role.getId());
            }
            return new HashSet<>(getRolesByUserId(userId));
        } else {
            throw new NotFoundException(String.format("User (id %d) or Role wasn't found", userId));
        }
    }

    @Transactional
    @Override
    public Set<Role> removeRole(Long userId, ERole roleName) {
        Optional<User> user = findById(userId);
        Role role = roleDao.findByName(roleName);
        if(user.isPresent() && role != null) {
            String query = "DELETE FROM user_roles WHERE user_id = ? AND role_id = ?";
            jdbcTemplate.update(query, userId, role.getId());
            return new HashSet<>(getRolesByUserId(userId));
        } else {
            throw new NotFoundException(String.format("User with id %d wasn't found", userId));
        }
    }

    @Transactional
    @Override
    public User removeAllRoles(Long userId) {
        Optional<User> user = findById(userId);
        if(user.isPresent()) {
            String deleteQuery = "DELETE FROM user_roles WHERE user_id = ?";
            jdbcTemplate.update(deleteQuery, userId);
            return user.get();
        }
        throw new NotFoundException(String.format("User with id %d wasn't found", userId));
    }

    @Transactional
    @Override
    public Set<Role> getRolesByUserId(Long userId) {
        if(userId != null && findById(userId).isPresent()) {
            String query = "SELECT r.* FROM roles r " +
                    "JOIN user_roles ur ON ur.role_id = r.id " +
                    "WHERE ur.user_id=?";
            return new HashSet<>(jdbcTemplate.query(query, roleMapper, userId));
        }
        throw new NotFoundException(String.format("User with id %d wasn't found", userId));
    }


//    private void processParserResults(User user, User savedUser) {
//        List<ParserResult> parserResults = user.getParserResults();
//        parserResults
//                .forEach(result -> {
//                    result.setUser(savedUser);
//                    if (Objects.nonNull(result.getUserParserSettings())) {
//                        processParserSettings(result);
//                    }
//                    parserResultDao.save(result);
//                });
//    }
//
//    private void processParserSettings(ParserResult result) {
//        UserParserSetting savedUserParserSetting = parserSettingsDao.save(result.getUserParserSettings());
//        result.getUserParserSettings()
//                .getElementLocators()
//                .forEach(elementLocator -> {
//                    elementLocator.setUserParserSetting(savedUserParserSetting);
//                    elementLocatorDao.save(elementLocator);
//                });
//        result.setUserParserSettings(savedUserParserSetting);
//    }
}
