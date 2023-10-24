package ru.researchser.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.researchser.mappers.ParserResultMapper;
import ru.researchser.mappers.UserParserSettingsMapper;
import ru.researchser.openapi.model.ParserResultOpenApi;
import ru.researchser.openapi.model.UserParserSettingsOpenApi;
import ru.researchser.repositories.ParserResultRepository;
import ru.researchser.repositories.UserParseSettingRepository;
import ru.researchser.services.interfaces.ParserService;
import ru.researchser.services.parser.ParserRunner;

import java.util.List;
@Service
@RequiredArgsConstructor
@Log4j
public class ParserServiceImpl implements ParserService {
    private final ParserRunner parserRunner;
    private final UserParserSettingsMapper parserSettingsMapper;
    private final ParserResultMapper parserResultMapper;
    private final UserParseSettingRepository parseSettingRepository;
    private final ParserResultRepository parserResultRepository;

    @Override
    public List<ParserResultOpenApi> getAllParserQueries() {
        return parserResultMapper.toOpenApi(parserResultRepository.findAll());
    }

    @Override
    public ParserResultOpenApi showParserSettings(Long id) {
        return parseSettingRepository.;
    }

    @Override
    public ResponseEntity<Void> setParserSettings(UserParserSettingsOpenApi userParserSettingsOpenApi) {
        return null;
    }

    @Override
    public ResponseEntity<Void> runParser(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<Resource> downloadFile(Long id) {
        return null;
    }
}
