package ru.researchser.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.researchser.models.EmailToken;

public interface EmailTokenRepository extends JpaRepository<EmailToken, Long> {
    EmailToken findByToken(String activationToken);
}
