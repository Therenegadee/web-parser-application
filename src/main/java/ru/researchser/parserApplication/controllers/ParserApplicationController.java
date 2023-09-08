package ru.researchser.parserApplication.controllers;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.researchser.parserApplication.ParserApplication;
import ru.researchser.parserApplication.models.settingsForParsing.UserParseSetting;
import ru.researchser.user.repositories.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/parser")
@RequiredArgsConstructor
public class ParserApplicationController {
    private final ParserApplication parserApplication;
    private final UserRepository userRepository;

    @PostMapping("/set-settings")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> setParserSettings(
            @RequestBody UserParseSetting userParseSetting,
            HttpSession session
    ) {

        session.setAttribute("userParserSettings", userParseSetting);

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, "/start-parse")
                .build();
    }

    @GetMapping("/start-parse")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> runParser(HttpSession session) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String currentPrincipalName = authentication.getName();
            UserParseSetting userParseSetting = userRepository
                    .findByUsername(currentPrincipalName)
                    .get()
                    .getUserParseSetting();
            String outputFilePath = parserApplication.runParser(userParseSetting);
            session.setAttribute("outputFilePath", outputFilePath);
            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .build();
        } else {
            return ResponseEntity
                    .status(401)
                    .build();
        }
    }

    @GetMapping("/download-parse-result")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<byte[]> downloadFile(HttpSession session) {
        String outputFilePath = session.getAttribute("outputFilePath").toString();
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
