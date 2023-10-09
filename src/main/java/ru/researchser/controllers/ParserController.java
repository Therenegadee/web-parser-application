package ru.researchser.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import ru.researchser.openapi.api.ParserApiDelegate;
import ru.researchser.openapi.model.MessageResponse;
import ru.researchser.openapi.model.ParserResult;
import ru.researchser.openapi.model.UserParserSettings;
import ru.researchser.parser.models.UserParseSetting;
import ru.researchser.parser.services.ParserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/parser")
@AllArgsConstructor
public class ParserController implements ParserApiDelegate {
    private final ParserService parserService;

    @Override
    public ResponseEntity<List<ParserResult>> getAllParserQueries() {
        return ParserApiDelegate.super.getAllParserQueries();
    }
    @Override
    @GetMapping
    public ResponseEntity<ParserResult> showParserSettings(@RequestParam("id") @Valid Long id) {
        return ParserApiDelegate.super.showParserSettings(id);
    }

    @Override
    @PostMapping("/settings")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> setParserSettings(@RequestBody UserParseSetting userParseSetting) {
        // TODO: fix with mapped object UserParseSetting
        return ResponseEntity
                .ok(new MessageResponse("The settings were set successfuly!"));
    }

    @Override
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> runParser(@RequestParam("id") @Valid Long id) {
        //TODO: finish the method logic
        UserParseSetting userParseSetting = new UserParseSetting();
        String outputFilePath = parserService.runParser(userParseSetting);
        //session.setAttribute("outputFilePath", outputFilePath);
        return ResponseEntity
                .ok(new MessageResponse("The information was collected successfuly!"));
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
