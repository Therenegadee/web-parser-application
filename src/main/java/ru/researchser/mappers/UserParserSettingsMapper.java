package ru.researchser.mappers;

import org.mapstruct.Mapper;
import ru.researchser.openapi.model.UserParserSettingsOpenApi;
import ru.researchser.models.parser.UserParserSetting;

@Mapper(componentModel = "spring")
public interface UserParserSettingsMapper {
    UserParserSetting toUserParseSetting(UserParserSettingsOpenApi userParserSettingsOpenApi);
    UserParserSettingsOpenApi toOpenApi(UserParserSetting userParserSetting);
}
