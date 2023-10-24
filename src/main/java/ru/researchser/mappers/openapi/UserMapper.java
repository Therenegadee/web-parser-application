package ru.researchser.mappers.openapi;

import org.mapstruct.Mapper;
import ru.researchser.openapi.model.UserOpenApi;
import ru.researchser.models.User;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {
    User toUser(UserOpenApi userOpenApi);
    List<User> toUser(List<UserOpenApi> userOpenApis);

    UserOpenApi toOpenApi(User user);
    List<UserOpenApi> toOpenApi(List<User> users);

}
