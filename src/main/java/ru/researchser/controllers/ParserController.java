package ru.researchser.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.researchser.openapi.api.ParserApiDelegate;
import ru.researchser.openapi.model.ParserResultOpenApi;
import ru.researchser.openapi.model.UserParserSettingsOpenApi;
import ru.researchser.services.interfaces.ParserService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/parser")
@AllArgsConstructor
@Log4j
public class ParserController implements ParserApiDelegate {
    private final ParserService parserService;

    @Override
    public ResponseEntity<List<ParserResultOpenApi>> getAllParserQueries() {
        return ResponseEntity
                .ok(parserService.getAllParserQueries().stream().toList());

    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ParserResultOpenApi> showParserResultsById(@PathVariable("id") @Valid Long id) {
        return ResponseEntity.ok(parserService.showParserResultsById(id));
    }

    @Override
    @PostMapping("/settings")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> setParserSettings(@RequestBody UserParserSettingsOpenApi userParserSettingsOpenApi) {
        return parserService.setParserSettings(userParserSettingsOpenApi);
    }

    @Override
    @PostMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> runParser(@PathVariable("id") @Valid Long id) {
        return parserService.runParser(id);
    }

    @Override
    @GetMapping("/{id}/download")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") @Valid Long id) {
        return parserService.downloadFile(id);
    }
}
