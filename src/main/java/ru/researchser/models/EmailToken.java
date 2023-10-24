package ru.researchser.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.researchser.models.enums.TokenType;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name="activation_token")
@Getter
@Setter
public class EmailToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_seq_generator")
    @SequenceGenerator(name = "token_seq_generator", sequenceName = "token_seq", allocationSize = 1)
    private Long id;
    private String token;
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;


    public EmailToken(User user) {
        this.user = user;
        token = UUID.randomUUID().toString();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, 2880);
        expirationDate = new Date(cal.getTime().getTime());
    }
}
