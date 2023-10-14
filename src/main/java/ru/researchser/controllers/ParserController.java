package ru.researchser.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.researchser.mappers.ParserResultMapper;
import ru.researchser.mappers.UserParserSettingsMapper;
import ru.researchser.models.parser.ParserResult;
import ru.researchser.models.user.User;
import ru.researchser.openapi.api.ParserApiDelegate;
import ru.researchser.openapi.model.ParserResultOpenApi;
import ru.researchser.openapi.model.UserParserSettingsOpenApi;
import ru.researchser.models.parser.UserParserSetting;
import ru.researchser.repositories.ParserResultRepository;
import ru.researchser.repositories.UserParseSettingRepository;
import ru.researchser.services.parser.ParserService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/parser")
@AllArgsConstructor
@Log4j
public class ParserController implements ParserApiDelegate {
    private final ParserService parserService;
    private final UserParserSettingsMapper parserSettingsMapper;
    private final ParserResultMapper parserResultMapper;
    private final UserParseSettingRepository parseSettingRepository;
    private final ParserResultRepository parserResultRepository;

    @Override
    public ResponseEntity<List<ParserResultOpenApi>> getAllParserQueries() {
        return ResponseEntity
                .ok(parserResultMapper.toOpenApi(parserResultRepository.findAll()));

    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ParserResultOpenApi> showParserSettings(@PathVariable("id") @Valid Long id) {
        return ParserApiDelegate.super.showParserSettings(id);
    }

    @Override
    @PostMapping("/settings")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> setParserSettings(@RequestBody UserParserSettingsOpenApi userParserSettingsOpenApi) {
        UserParserSetting userParserSettings = parserSettingsMapper.toUserParseSetting(userParserSettingsOpenApi);
        parseSettingRepository.save(userParserSettings);
        return ResponseEntity
                .ok()
                .build();
    }
    @Override
    @PostMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> runParser(@PathVariable("id") @Valid Long id) {
        Optional<UserParserSetting> userParserSettingOpt = parseSettingRepository.findById(id);
        UserParserSetting userParserSetting;
        if (userParserSettingOpt.isPresent()) {
            userParserSetting = userParserSettingOpt.get();
        } else {
            log.debug("userParserSetting was empty, no such object in db");
            return ResponseEntity
                    .badRequest()
                    .build();
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();

        Path path = Paths.get(parserService.runParser(userParserSetting, username));

        byte[] byteData;
        try {
            byteData = Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String base64String = Base64.getEncoder().encodeToString(byteData);

        ParserResult parserResult = ParserResult
                .builder()
                .linkToDownloadResults(base64String)
                .userParserSettings(parserSettingsMapper.toOpenApi(userParserSetting))
                .build();
        parserResultRepository.save(parserResult);
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity
                .ok()
                .build();
    }

    @Override
    @GetMapping("/{id}/download")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") @Valid Long id) {
        Optional<ParserResult> parserResultOpt = parserResultRepository.findById(id);
        ParserResult parserResult;
        if(parserResultOpt.isPresent()) {
            parserResult = parserResultOpt.get();
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (parserResult != null) {
            String base64String = parserResult.getLinkToDownloadResults();
            byte[] byteData = Base64.getDecoder().decode(base64String);
            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(byteData));
            String formatOfFile = parserResult.getUserParserSettings().getOutputFileType().toString().toLowerCase();
            String headerValues = "attachment; filename=dataharvest.".concat(formatOfFile);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, headerValues)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
