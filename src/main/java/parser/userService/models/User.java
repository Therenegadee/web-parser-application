package parser.userService.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import parser.userService.models.enums.ActivationStatus;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
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
    private Set<Role> roles = new HashSet<>();
    private ActivationStatus activationStatus;
    private String telegramUserId;

    public User (String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }



}
