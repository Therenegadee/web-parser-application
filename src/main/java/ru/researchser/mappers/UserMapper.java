package ru.researchser.mappers;

import org.mapstruct.Mapper;
import ru.researchser.DTOs.UserDTO;
import ru.researchser.openapi.model.UserOpenApi;
import ru.researchser.models.user.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserOpenApi userOpenApi);
    User toUser(UserDTO userDTO);
    List<User> toUser(List<UserDTO> userDTOs);

    UserDTO toDTO(User user);
    List<UserDTO> toDTO(List<User> users);
    UserOpenApi toOpenApi(User user);
}
