package ru.researchser.models.parser;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Data;
import ru.researchser.services.parser.logic.element.CssSelectorElement;
import ru.researchser.services.parser.logic.element.ElementType;
import ru.researchser.services.parser.logic.element.TagAttrElement;
import ru.researchser.services.parser.logic.element.XPathElement;

import static jakarta.persistence.InheritanceType.JOINED;

@Data
@Entity
@Table(name = "ElementLocator")
@Inheritance(strategy = JOINED)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = XPathElement.class, name = "XPath"),
        @JsonSubTypes.Type(value = CssSelectorElement.class, name = "CSS Selector"),
        @JsonSubTypes.Type(value = TagAttrElement.class, name = "Tag+Attribute"),
})
public class ElementLocator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private ElementType type;
    private String pathToLocator;
    private String extraPointer; // for Tag + Attribute
    @ManyToOne
    private UserParserSetting userParserSetting;
}
