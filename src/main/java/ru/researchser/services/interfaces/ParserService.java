package ru.researchser.services.interfaces;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import ru.researchser.openapi.model.ParserResultOpenApi;
import ru.researchser.openapi.model.UserParserSettingsOpenApi;

import java.util.List;

public interface ParserService {
    List<ParserResultOpenApi> getAllParserQueries();

    ParserResultOpenApi showParserResultsById(Long id);

    ResponseEntity<Void> setParserSettings(UserParserSettingsOpenApi userParserSettingsOpenApi);

    ResponseEntity<Void> runParser(Long id);

    ResponseEntity<Resource> downloadFile(Long id);
}
