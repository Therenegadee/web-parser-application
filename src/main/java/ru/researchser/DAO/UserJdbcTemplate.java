package ru.researchser.DAO;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.researchser.DAO.interfaces.UserDao;
import ru.researchser.models.User;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserJdbcTemplate implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByTelegramId(String telegramId) {
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public User updateById(Long id, User user) {
        return null;
    }

    @Override
    public int deleteById(Long id) {
        return 0;
    }

    @Override
    public int delete(User user) {
        return 0;
    }

    @Override
    public int deleteAll() {
        return 0;
    }

    @Override
    public Boolean existsByUsername(String username) {
        return null;
    }

    @Override
    public Boolean existsByEmail(String email) {
        return null;
    }
}
