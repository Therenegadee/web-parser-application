package ru.researchser.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.researchser.models.ParserResult;

@Repository
public interface ParserResultRepository extends JpaRepository<ParserResult, Long> {
}
