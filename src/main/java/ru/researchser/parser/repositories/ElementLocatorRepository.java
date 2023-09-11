package ru.researchser.parser.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.researchser.parser.models.ElementLocator;

public interface ElementLocatorRepository extends JpaRepository<ElementLocator, Long> {
}
