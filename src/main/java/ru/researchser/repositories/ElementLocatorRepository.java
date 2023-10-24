package ru.researchser.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.researchser.models.ElementLocator;

public interface ElementLocatorRepository extends JpaRepository<ElementLocator, Long> {
}
