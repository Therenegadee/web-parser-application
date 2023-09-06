package ru.researchser.parserApplication.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.researchser.parserApplication.ParserApplication;
import ru.researchser.parserApplication.models.elementLocator.UserParseSetting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/parser")
@RequiredArgsConstructor
public class ParserApplicationController {
    private final ParserApplication parserApplication;

    @PostMapping("/set-settings")
    public UserParseSetting setParserSettings(@RequestBody UserParseSetting userParseSetting) {
        return userParseSetting;
    }

    @GetMapping("/start-parse")
    public ResponseEntity<String> runParser(@ModelAttribute UserParseSetting userParseSetting,
                                            RedirectAttributes redirectAttributes) {

        String outputFilePath = parserApplication.runParser(userParseSetting);
        redirectAttributes.addFlashAttribute("outputFilePath", outputFilePath);

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, "/parser/download-result")
                .build();
    }

    @GetMapping("/download-parse-result")
    public ResponseEntity<byte[]> downloadFile(@RequestParam(name = "outputFilePath") String outputFilePath){
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
