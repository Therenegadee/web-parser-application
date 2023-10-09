package ru.researchser.mappers;

import org.mapstruct.Mapper;
import ru.researchser.openapi.model.JwtResponseOpenApi;
import ru.researchser.services.security.payloads.response.JwtResponse;

@Mapper(componentModel = "spring")
public interface JwtResponseMapper {
    JwtResponse toJwtResponse(JwtResponseOpenApi jwtResponseOpenApi);
    JwtResponseOpenApi toOpenApi(JwtResponse jwtResponse);
}
