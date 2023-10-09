package ru.researchser.models.user;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private ERoleOpenApi name;

    public Role(ERoleOpenApi name) {
        this.name = name;
    }
}
