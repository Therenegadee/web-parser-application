package ru.researchser.models.parser;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.researchser.openapi.model.UserParserSettingsOpenApi;

@Data
@Entity
@Table(name = "parser_results")
@AllArgsConstructor
@NoArgsConstructor
public class ParserResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UserParserSettingsOpenApi userParserSettings;
    private String linkToDownloadResults;
}
