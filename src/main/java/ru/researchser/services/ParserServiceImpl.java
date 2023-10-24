package ru.researchser.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.researchser.exceptions.NotFoundException;
import ru.researchser.mappers.openapi.ParserResultMapper;
import ru.researchser.mappers.openapi.UserParserSettingsMapper;
import ru.researchser.models.ParserResult;
import ru.researchser.models.User;
import ru.researchser.models.UserParserSetting;
import ru.researchser.openapi.model.ParserResultOpenApi;
import ru.researchser.openapi.model.UserParserSettingsOpenApi;
import ru.researchser.DAO.interfaces.ParserResultDao;
import ru.researchser.DAO.interfaces.UserParserSettingsDao;
import ru.researchser.services.interfaces.ParserService;
import ru.researchser.services.parser.ParserRunner;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j
public class ParserServiceImpl implements ParserService {
    private final ParserRunner parserRunner;
    private final UserParserSettingsMapper parserSettingsMapper;
    private final ParserResultMapper parserResultMapper;
    private final UserParserSettingsDao parseSettingRepository;
    private final ParserResultDao parserResultDao;

    @Override
    @Transactional
    public List<ParserResultOpenApi> getAllParserQueries() {
        return parserResultMapper.toOpenApi(parserResultDao.findAll());
    }

    @Override
    @Transactional
    public ParserResultOpenApi showParserResultsById(Long id) {
        Optional<ParserResult> parserResultOpt = parserResultDao.findById(id);
        if (parserResultOpt.isPresent()) {
            return parserResultMapper.toOpenApi(parserResultOpt.get());
        }
        else {
            throw new NotFoundException(String.format("Parser results with id %d wasn't found", id));
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Void> setParserSettings(UserParserSettingsOpenApi userParserSettingsOpenApi) {
        UserParserSetting userParserSettings = parserSettingsMapper.toUserParseSetting(userParserSettingsOpenApi);
        parseSettingRepository.save(userParserSettings);
        return ResponseEntity
                .ok()
                .build();
    }

    @Override
    public ResponseEntity<Void> runParser(Long id) {
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

        Path path = Paths.get(parserRunner.runParser(userParserSetting, username));

        //TODO: добавить файл сервис

//        ParserResult parserResult = ParserResult
//                .builder()
//                .linkToDownloadResults(base64String)
//                .userParserSettings(parserSettingsMapper.toOpenApi(userParserSetting))
//                .build();
//        parserResultRepository.save(parserResult);
//        try {
//            Files.delete(path);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        return ResponseEntity
                .ok()
                .build();
    }

    @Override
    public ResponseEntity<Resource> downloadFile(Long id) {
        return null;
    }
}
