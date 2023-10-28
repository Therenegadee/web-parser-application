package parser.userService.mappers.openapi;

import parser.userService.models.enums.ERole;
import parser.userService.openapi.model.ERoleOpenApi;

public interface RoleMapper {

    ERoleOpenApi toOpenApi(ERole eRole);

    ERole toERole(ERoleOpenApi eRoleOpenApi);
}
