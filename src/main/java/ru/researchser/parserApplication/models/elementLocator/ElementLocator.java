package ru.researchser.parserApplication.models.elementLocator;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.UUID;

import static jakarta.persistence.InheritanceType.JOINED;

@Data
@Entity
@Inheritance(strategy = JOINED)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = XPathElement.class, name = "XPath"),
        @JsonSubTypes.Type(value = CssSelectorElement.class, name = "CSS Selector"),
        @JsonSubTypes.Type(value = TagAttrElement.class, name = "Tag+Attribute"),
})
public abstract class ElementLocator {
    @Id
    private UUID id;
    private String pathToLocator;
}
