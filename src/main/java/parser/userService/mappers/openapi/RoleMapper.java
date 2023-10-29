package parser.userService.mappers.openapi;

import org.mapstruct.Mapper;
import org.mapstruct.ValueMapping;
import org.mapstruct.ValueMappings;
import parser.userService.models.enums.ERole;
import user.openapi.model.ERoleOpenApi;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @ValueMappings({
            @ValueMapping(source = "ROLE_USER", target = "USER"),
            @ValueMapping(source = "ROLE_ADMIN", target = "ADMIN")
    })
    ERoleOpenApi toOpenApi(ERole eRole);

    @ValueMappings({
            @ValueMapping(source = "USER", target = "ROLE_USER"),
            @ValueMapping(source = "ADMIN", target = "ROLE_ADMIN")
    })
    ERole toERole(ERoleOpenApi eRoleOpenApi);
}
