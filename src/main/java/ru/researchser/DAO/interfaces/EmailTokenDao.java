package ru.researchser.DAO.interfaces;

import ru.researchser.models.ElementLocator;
import ru.researchser.models.EmailToken;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EmailTokenDao {
    Optional<EmailToken> findByToken(String activationToken);
    Optional<EmailToken> findById(Long id);
    Set<EmailToken> findAll();
    EmailToken save(EmailToken emailToken);
    EmailToken update(EmailToken emailToken);
    EmailToken updateById(Long id, EmailToken emailToken);
    int delete(EmailToken emailToken);
    int deleteById(Long id);
    int deleteAllExpired(Date date);
}
