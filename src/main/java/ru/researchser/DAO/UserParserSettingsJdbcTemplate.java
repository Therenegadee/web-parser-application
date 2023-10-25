package ru.researchser.DAO;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.researchser.DAO.interfaces.UserParserSettingsDao;
import ru.researchser.models.UserParserSetting;

import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class UserParserSettingsJdbcTemplate implements UserParserSettingsDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<UserParserSetting> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<UserParserSetting> findByParserResultId(Long id) {
        return Optional.empty();
    }

    @Override
    public UserParserSetting save(UserParserSetting userParserSetting) {
        return null;
    }

    @Override
    public UserParserSetting update(UserParserSetting userParserSetting) {
        return null;
    }

    @Override
    public UserParserSetting updateById(Long id, UserParserSetting userParserSetting) {
        return null;
    }

    @Override
    public Set<UserParserSetting> findAll() {
        return null;
    }

    @Override
    public Set<UserParserSetting> findAllByUserId(Long id) {
        return null;
    }

    @Override
    public int deleteById(Long id) {
        return 0;
    }

    @Override
    public int delete(UserParserSetting userParserSetting) {
        return 0;
    }

    @Override
    public int deleteAll() {
        return 0;
    }
}
