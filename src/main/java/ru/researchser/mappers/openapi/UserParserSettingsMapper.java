package ru.researchser.mappers.openapi;

import org.mapstruct.Mapper;
import ru.researchser.openapi.model.UserParserSettingsOpenApi;
import ru.researchser.models.UserParserSetting;

@Mapper(componentModel = "spring")
public interface UserParserSettingsMapper {
    UserParserSetting toUserParseSetting(UserParserSettingsOpenApi userParserSettingsOpenApi);
    UserParserSettingsOpenApi toOpenApi(UserParserSetting userParserSetting);
}
