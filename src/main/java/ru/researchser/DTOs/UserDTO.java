package ru.researchser.DTOs;

import ru.researchser.models.user.Role;
import ru.researchser.openapi.model.UserStatusOpenApi;

import java.util.Set;


public record UserDTO(
        Long id,
        String username,
        String email,
        Set<Role> roles,
        UserStatusOpenApi userStatus,
        String telegramUserId
) {}
