package ru.researchser.parserApplication.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.researchser.parserApplication.ParserApplication;

@RestController
@RequiredArgsConstructor
public class ParserApplicationController {
    private final ParserApplication parserApplication;
    private final UserParseSetting userParseSetting;

    @GetMapping("/parser/start-parse")
    public String runParser() {
        parserApplication.runParser(userParseSetting);
        return "redirect:/api/parser/wait-for-result";
    }

    @PostMapping("/parser/set-settings")
    public String setParserSettings(@RequestBody UserParseSetting userParseSetting) {
        return "redirect:/api/parser/start-parse";
    }

}
