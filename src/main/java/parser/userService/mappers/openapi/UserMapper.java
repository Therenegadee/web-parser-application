package parser.userService.mappers.openapi;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import parser.userService.openapi.model.UserOpenApi;
import parser.userService.models.User;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {

    User toUser(UserOpenApi userOpenApi);
    List<User> toUser(List<UserOpenApi> userOpenApis);
    Set<User> toUser(Set<UserOpenApi> userOpenApis);
    UserOpenApi toOpenApi(User user);
    List<UserOpenApi> toOpenApi(List<User> users);
    Set<UserOpenApi> toOpenApi(Set<User> users);

}
