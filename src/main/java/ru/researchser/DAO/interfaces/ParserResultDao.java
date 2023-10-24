package ru.researchser.DAO.interfaces;

import org.springframework.stereotype.Repository;
import ru.researchser.models.ElementLocator;
import ru.researchser.models.ParserResult;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ParserResultDao {
    Optional<ParserResult> findById(Long id);
    Optional<ParserResult> findByParserSettingsId(Long id);
    ParserResult save(ParserResult parserResult);

    ParserResult update(ParserResult parserResult);

    ParserResult updateById(Long id, ParserResult parserResult);

    Set<ParserResult> findAll();

    Set<ParserResult> findAllByUserId(Long id);

    int deleteById(Long id);

    int delete(ParserResult parserResult);

    int deleteAll();
}
