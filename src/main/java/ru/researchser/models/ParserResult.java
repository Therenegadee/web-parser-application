package ru.researchser.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.researchser.openapi.model.UserParserSettingsOpenApi;

@Data
@Entity
@Builder
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
