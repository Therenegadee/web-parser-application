package ru.researchser.parserApplication.models.parsingResult;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class ParsingResult {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String url;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parsing_result_id")
    private List<ColumnValue> parseResult;


}
