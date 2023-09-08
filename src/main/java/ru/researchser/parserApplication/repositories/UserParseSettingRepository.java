package ru.researchser.parserApplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.researchser.parserApplication.models.settingsForParsing.UserParseSetting;

@Repository
public interface UserParseSettingRepository extends JpaRepository<UserParseSetting, Long> {
}
