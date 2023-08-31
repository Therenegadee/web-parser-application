package ru.researchser.parserApplication.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.researchser.parserApplication.models.parsingResult.ColumnValue;
import ru.researchser.parserApplication.models.parsingResult.ParsingResult;
import ru.researchser.parserApplication.repositories.ParsingResultRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParserService {

    private final ParsingResultRepository parsingResultRepository;

    public void saveParsingResult(List<String> resultRow, String link) {
        ParsingResult parsingResult = new ParsingResult();
        List<ColumnValue> columnValues = new ArrayList<>();
        for (int i = 0; i < resultRow.size(); i++) {
            columnValues.add(ColumnValue.builder()
                    .columnNumber(i)
                    .value(resultRow.get(i))
                    .build());
        }
        parsingResult.setUrl(link);
        parsingResult.setParseResult(columnValues);
        parsingResultRepository.save(parsingResult);
    }

}
