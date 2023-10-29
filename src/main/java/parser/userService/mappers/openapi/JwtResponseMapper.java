package parser.userService.mappers.openapi;

import org.mapstruct.Mapper;
import parser.userService.security.payloads.response.JwtResponse;
import user.openapi.model.JwtResponseOpenApi;

@Mapper(componentModel = "spring")
public interface JwtResponseMapper {
    JwtResponse toJwtResponse(JwtResponseOpenApi jwtResponseOpenApi);
    JwtResponseOpenApi toOpenApi(JwtResponse jwtResponse);
}
