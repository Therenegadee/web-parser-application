package ru.researchser.parser.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Data;
import ru.researchser.parser.logic.element.CssSelectorElement;
import ru.researchser.parser.logic.element.ElementType;
import ru.researchser.parser.logic.element.TagAttrElement;
import ru.researchser.parser.logic.element.XPathElement;

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
public abstract class ElementLocator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private ElementType type;
    private String pathToLocator;
    private String extraPointer; // for Tag + Attribute
    @ManyToOne
    private UserParseSetting userParseSetting;
}
