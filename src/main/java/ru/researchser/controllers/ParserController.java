package ru.researchser.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.researchser.parser.models.UserParseSetting;
import ru.researchser.parser.services.ParserService;
import ru.researchser.security.payloads.response.MessageResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/parser")
@Tag(name = "Parser", description = "Parser API")
@SecurityRequirement(name = "bearerAuth")
@AllArgsConstructor
public class ParserController {
    private final ParserService parserService;

    @PostMapping("/settings")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(
            summary = "Set up settings for parsing",
            operationId = "setParserSettings"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account was confirmed successfully"),
            @ApiResponse(responseCode = "400", description = "Account is already confirmed!"),
            @ApiResponse(responseCode = "404", description = "The link isn't valid. User with such user id not found.", content = {@Content(schema = @Schema())})
    })
    public ResponseEntity<?> setParserSettings(
            @RequestBody UserParseSetting userParseSetting,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession();
        session.setAttribute("userParserSettings", userParseSetting);
        return ResponseEntity
                .ok(new MessageResponse("The settings were set successfuly!"));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(
            summary = "Start parsing",
            operationId = "runParser"
    )
    public ResponseEntity<?> runParser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserParseSetting userParseSetting = (UserParseSetting) session.getAttribute("userParserSettings");
        String outputFilePath = parserService.runParser(userParseSetting);
        session.setAttribute("outputFilePath", outputFilePath);
        return ResponseEntity
                .ok(new MessageResponse("The information was collected successfuly!"));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(
            summary = "Download parser results file",
            operationId = "downloadFile"
    )
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
