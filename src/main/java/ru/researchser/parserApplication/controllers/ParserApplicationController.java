package ru.researchser.parserApplication.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.researchser.parserApplication.ParserApplication;

@RestController
@RequiredArgsConstructor
public class ParserApplicationController {
    private final ParserApplication parserApplication;

    @GetMapping("/parser/start-parse")
    public String runParser() {
        parserApplication.runParser();
        return "Parser executed!";
    }

    @PostMapping("/parser/settings-for-parsing")
    public String setParserSettings(@RequestBody ParseSetting parseSetting) {

    }

}
