package ru.researchser.DAO.interfaces;

import ru.researchser.models.ElementLocator;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ElementLocatorDao {
    Optional<ElementLocator> findById(Long id);

    ElementLocator save(ElementLocator elementLocator);

    ElementLocator update(ElementLocator elementLocator);

    ElementLocator updateById(Long id, ElementLocator elementLocator);

    Set<ElementLocator> findAll();

    Set<ElementLocator> findAllByParserSettingsId(Long id);

    int deleteById(Long id);

    int delete(ElementLocator elementLocator);

    int deleteAll();

}
