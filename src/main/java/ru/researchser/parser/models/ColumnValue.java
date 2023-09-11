package ru.researchser.parser.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ColumnValue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    private int columnNumber;
    private String value;
}
