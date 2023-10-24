package ru.researchser.mappers.openapi;

import ru.researchser.models.enums.ERole;
import ru.researchser.openapi.model.ERoleOpenApi;

public interface RoleMapper {

    ERoleOpenApi toOpenApi(ERole eRole);

    ERole toERole(ERoleOpenApi eRoleOpenApi);
}
