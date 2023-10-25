package ru.researchser.mappers.openapi;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.researchser.openapi.model.UserOpenApi;
import ru.researchser.models.User;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {RoleMapper.class, ParserResultIdMapper.class})
public interface UserMapper {

    @Mapping(target = "parserResults", source = "parserResultsIds")
    User toUser(UserOpenApi userOpenApi);
    @Mapping(target = "parserResults", source = "parserResultsIds")
    List<User> toUser(List<UserOpenApi> userOpenApis);
    @Mapping(target = "parserResults", source = "parserResultsIds")
    Set<User> toUser(Set<UserOpenApi> userOpenApis);

    @Mapping(target = "parserResultsIds", source = "parserResults")
    UserOpenApi toOpenApi(User user);
    @Mapping(target = "parserResultsIds", source = "parserResults")
    List<UserOpenApi> toOpenApi(List<User> users);
    @Mapping(target = "parserResultsIds", source = "parserResults")
    Set<UserOpenApi> toOpenApi(Set<User> users);

}
