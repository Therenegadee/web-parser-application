package ru.researchser.mappers;

import org.mapstruct.Mapper;
import ru.researchser.openapi.model.ParserResultOpenApi;
import ru.researchser.models.parser.ParserResult;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ParserResultMapper {
    ParserResult toParserResult(ParserResultOpenApi parserResultOpenApi);
    ParserResultOpenApi toOpenApi(ParserResult parserResult);

    List<ParserResult> toParserResult(List<ParserResultOpenApi> parserResultsOpenApi);
    List<ParserResultOpenApi> toOpenApi(List<ParserResult> parserResults);
}
