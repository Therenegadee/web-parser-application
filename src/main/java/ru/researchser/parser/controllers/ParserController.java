package ru.researchser.parser.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.researchser.parser.models.UserParseSetting;
import ru.researchser.parser.services.ParserService;
import ru.researchser.user.repositories.UserRepository;
import ru.researchser.user.security.payloads.response.MessageResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/parser")
@RequiredArgsConstructor
public class ParserController {
    private final ParserService parserService;
    private final UserRepository userRepository;

    @PostMapping("/set-settings")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> setParserSettings(
            @RequestBody UserParseSetting userParseSetting,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession();
        session.setAttribute("userParserSettings", userParseSetting);
        return ResponseEntity
                .ok(new MessageResponse("The settings were set successfuly!"));
    }

    @GetMapping("/start-parse")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> runParser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserParseSetting userParseSetting = (UserParseSetting) session.getAttribute("userParserSettings");
        String outputFilePath = parserService.runParser(userParseSetting);
        session.setAttribute("outputFilePath", outputFilePath);
        return ResponseEntity
                .ok(new MessageResponse("The information was collected successfuly!"));
    }

    @GetMapping("/download-parse-result")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<byte[]> downloadFile(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String outputFilePath = (String) session.getAttribute("outputFilePath");
        if (outputFilePath != null) {
            try {
                Path path = Path.of(outputFilePath);
                byte[] fileContent = Files.readAllBytes(path);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", path.getFileName().toString());
                Files.delete(path);
                return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>(new byte[0], HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
