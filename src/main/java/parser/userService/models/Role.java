package parser.userService.models;

import lombok.*;
import parser.userService.models.enums.ERole;

@Getter
@Setter
@NoArgsConstructor
public class Role {
    private Long id;
    private ERole name;

    public Role(ERole name) {
        this.name = name;
    }
}
