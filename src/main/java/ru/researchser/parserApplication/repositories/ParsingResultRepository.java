package ru.researchser.parserApplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.researchser.parserApplication.models.parsingResult.ParsingResult;

@Repository
public interface ParsingResultRepository extends JpaRepository<ParsingResult, Long> {
}
