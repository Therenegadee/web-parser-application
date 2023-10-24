package ru.researchser.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.researchser.services.parser.logic.element.CssSelectorElement;
import ru.researchser.models.enums.ElementType;
import ru.researchser.services.parser.logic.element.TagAttrElement;
import ru.researchser.services.parser.logic.element.XPathElement;

@Getter
@Setter
@Builder
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = XPathElement.class, name = "XPath"),
        @JsonSubTypes.Type(value = CssSelectorElement.class, name = "CSS Selector"),
        @JsonSubTypes.Type(value = TagAttrElement.class, name = "Tag+Attribute"),
})
public class ElementLocator {
    private Long id;
    private ElementType type;
    private String pathToLocator;
    private String extraPointer; // for Tag + Attribute
    private UserParserSetting userParserSetting;
}
