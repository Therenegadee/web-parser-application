package ru.researchser.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.researchser.models.enums.ERole;
import ru.researchser.openapi.model.ERoleOpenApi;

@Entity
@Table(name = "roles",
        uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
@Data
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, unique = true)
    private ERole name;

    public Role(ERole name) {
        this.name = name;
    }
}
