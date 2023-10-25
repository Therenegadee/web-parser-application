package ru.researchser.models;

import lombok.*;
import ru.researchser.models.enums.ERole;

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
