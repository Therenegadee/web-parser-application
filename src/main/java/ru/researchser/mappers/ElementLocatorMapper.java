package ru.researchser.mappers;

import org.mapstruct.Mapper;
import ru.researchser.openapi.model.ElementLocatorOpenApi;
import ru.researchser.models.ElementLocator;

@Mapper(componentModel = "spring")
public interface ElementLocatorMapper {
    ElementLocator toElementLocator(ElementLocatorOpenApi elementLocatorOpenApi);
    ElementLocatorOpenApi toOpenApi(ElementLocator elementLocator);
}
