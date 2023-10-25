package ru.researchser.mappers.openapi;

import org.mapstruct.Mapper;
import ru.researchser.openapi.model.ParserResultOpenApi;
import ru.researchser.models.ParserResult;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ParserResultMapper {
    ParserResult toParserResult(ParserResultOpenApi parserResultOpenApi);
    ParserResultOpenApi toOpenApi(ParserResult parserResult);

    List<ParserResult> toParserResult(List<ParserResultOpenApi> parserResultsOpenApi);
    List<ParserResultOpenApi> toOpenApi(List<ParserResult> parserResults);
    Set<ParserResult> toParserResult(Set<ParserResultOpenApi> parserResultsOpenApi);
    Set<ParserResultOpenApi> toOpenApi(Set<ParserResult> parserResults);
}
