package ru.researchser.parser.logic.parameter;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TwoParseParameters implements ParseParameter {
    private final String parameter1;
    private final String parameter2;
}
