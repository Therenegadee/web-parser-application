package parser.userService.DAO.interfaces;

import org.springframework.stereotype.Repository;
import parser.userService.models.EmailToken;

import java.sql.Date;
import java.util.Optional;
import java.util.Set;

@Repository
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
