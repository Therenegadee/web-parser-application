package ru.researchser.parserApplication.models.elementLocator;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import ru.researchser.parserApplication.models.elementLocator.DTOs.ElementType;
import ru.researchser.parserApplication.models.elementLocator.DTOs.ParseAlgorithm;
import ru.researchser.parserApplication.models.elementLocator.DTOs.ParseParameter;

import java.util.*;
public class ParseElement {

    private final ParseAlgorithm parseAlgorithm;
    private final ElementType type;
    private final List<ParseParameter> parameters;

    @Autowired
    public ParseElement(WebDriver driver, ElementType type, List<ParseParameter> parameters) {
        this.type = type;
        this.parameters = parameters;
        this.parseAlgorithm = switch (this.type) {
            case XPATH -> new XPathElement(driver);
            case CSS -> new CssSelectorElement(driver);
            case TAG_ATTR -> new TagAttrElement(driver);
        };
    }

    public String parseByParameters(String url) {
        return parameters
                .stream()
                .map(parameter -> parseAlgorithm.parseByParameters(parameter, url))
                .findFirst()
                .get();
    }
}
