package ru.researchser.user.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.researchser.parser.models.UserParseSetting;
import ru.researchser.user.models.enums.UserStatus;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "telegramUserId")
        })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<Role> roles = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @OneToMany (fetch = FetchType.LAZY)
    @JoinColumn(name = "user_parse_settings_id")
    private List<UserParseSetting> userParseSetting;

    private String telegramUserId;

    public User (String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

}
