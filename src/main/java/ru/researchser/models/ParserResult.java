package ru.researchser.models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParserResult {
    private Long id;
    private UserParserSetting userParserSettings;
    private String linkToDownloadResults;
}
