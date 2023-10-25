package ru.researchser.DAO.interfaces;

import org.springframework.stereotype.Repository;
import ru.researchser.models.ParserResult;
import ru.researchser.models.UserParserSetting;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserParserSettingsDao {
    Optional<UserParserSetting> findById(Long id);
    Optional<UserParserSetting> findByParserResultId(Long id);
    UserParserSetting save(UserParserSetting userParserSetting);

    UserParserSetting update(UserParserSetting userParserSetting);

    UserParserSetting updateById(Long id, UserParserSetting userParserSetting);

    Set<UserParserSetting> findAll();

    Set<UserParserSetting> findAllByUserId(Long id);

    int deleteById(Long id);

    int delete(UserParserSetting userParserSetting);

    int deleteAll();
}
