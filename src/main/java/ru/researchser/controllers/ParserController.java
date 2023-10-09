package ru.researchser.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.researchser.mappers.UserParserSettingsMapper;
import ru.researchser.models.parser.ParserResult;
import ru.researchser.models.user.User;
import ru.researchser.openapi.api.ParserApiDelegate;
import ru.researchser.openapi.model.MessageResponseOpenApi;
import ru.researchser.openapi.model.ParserResultOpenApi;
import ru.researchser.openapi.model.UserParserSettingsOpenApi;
import ru.researchser.models.parser.UserParserSetting;
import ru.researchser.repositories.ParserResultRepository;
import ru.researchser.repositories.UserParseSettingRepository;
import ru.researchser.services.parser.ParserService;

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
    private final UserParseSettingRepository parseSettingRepository;
    private final ParserResultRepository parserResultRepository;

    @Override
    public ResponseEntity<List<ParserResultOpenApi>> getAllParserQueries() {
        return ParserApiDelegate.super.getAllParserQueries();
    }

    @Override
    @GetMapping
    public ResponseEntity<ParserResultOpenApi> showParserSettings(@RequestParam("id") @Valid Long id) {
        return ParserApiDelegate.super.showParserSettings(id);
    }

    @Override
    @PostMapping("/settings")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<MessageResponseOpenApi> setParserSettings(@RequestBody UserParserSettingsOpenApi userParserSettingsOpenApi) {
        UserParserSetting userParserSettings = parserSettingsMapper.toUserParseSetting(userParserSettingsOpenApi);
        parseSettingRepository.save(userParserSettings);
        return ResponseEntity
                .ok(new MessageResponseOpenApi("The settings were set successfuly!"));
    }

    @Override
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<MessageResponseOpenApi> runParser(@RequestParam("id") @Valid Long id) {
        Optional<UserParserSetting> userParserSettingOpt = parseSettingRepository.findById(id);
        UserParserSetting userParserSetting;
        if (userParserSettingOpt.isPresent()) {
            userParserSetting = userParserSettingOpt.get();
        } else {
            log.debug("userParserSetting was empty, no such object in db");
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponseOpenApi("This Parser Settings wasn't found"));
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
        String outputFilePath = parserService.runParser(userParserSetting, username);
        ParserResult parserResult = ParserResult
                .builder()
                .linkToDownloadResults(outputFilePath)
                .userParserSettings(parserSettingsMapper.toOpenApi(userParserSetting))
                .build();
        parserResultRepository.save(parserResult);
        return ResponseEntity
                .ok(new MessageResponseOpenApi("The information was collected successfuly!"));
    }

//    @GetMapping
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
//    public ResponseEntity<byte[]> downloadFile() {
//        //todo: fix
//        HttpSession session = new HttpSession().toString();
//        String outputFilePath = (String) session.getAttribute("outputFilePath");
//        if (outputFilePath != null) {
//            try {
//                Path path = Path.of(outputFilePath);
//                byte[] fileContent = Files.readAllBytes(path);
//                HttpHeaders headers = new HttpHeaders();
//                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//                headers.setContentDispositionFormData("attachment", path.getFileName().toString());
//                Files.delete(path);
//                return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return new ResponseEntity<>(new byte[0], HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
