package ru.researchser.user.models;

import jakarta.persistence.*;
import lombok.Data;
import ru.researchser.user.models.enums.ERole;

@Entity
@Table(name = "roles")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, unique = true)
    private ERole name;

}
