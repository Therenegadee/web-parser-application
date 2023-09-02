package ru.researchser.parserApplication.models.elementLocator.DTOs;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class OneParseParameter implements ParseParameter {
    private final String parameter;
}
