package ru.researchser.mappers;

import org.mapstruct.Mapper;
import ru.researchser.openapi.model.ParserResultOpenApi;
import ru.researchser.models.parser.ParserResult;

@Mapper
public interface ParserResultMapper {
    ParserResult toParserResult(ParserResultOpenApi parserResultOpenApi);
    ParserResultOpenApi toOpenApi(ParserResult parserResult);
}
