package ru.researchser.parser.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.researchser.parser.models.ParsingResult;

@Repository
public interface ParsingResultRepository extends JpaRepository<ParsingResult, Long> {
}
