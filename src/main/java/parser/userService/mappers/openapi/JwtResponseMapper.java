package parser.userService.mappers.openapi;

import org.mapstruct.Mapper;
import parser.userService.openapi.model.JwtResponseOpenApi;
import parser.userService.security.payloads.response.JwtResponse;

@Mapper(componentModel = "spring")
public interface JwtResponseMapper {
    JwtResponse toJwtResponse(JwtResponseOpenApi jwtResponseOpenApi);
    JwtResponseOpenApi toOpenApi(JwtResponse jwtResponse);
}
