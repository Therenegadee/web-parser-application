package ru.researchser.parserApplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.researchser.parserApplication.models.elementLocator.ElementLocator;

public interface ElementLocatorRepository extends JpaRepository<ElementLocator, Long> {
}
