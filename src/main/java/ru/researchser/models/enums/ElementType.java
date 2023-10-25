package ru.researchser.models.enums;

public enum ElementType {
    XPATH ("XPATH"),
    TAG_ATTR ("TAG_ATTR"),
    CSS ("CSS");

    private final String value;

    ElementType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ElementType fromValue(String value) {
        for (ElementType elementType : ElementType.values()) {
            if (elementType.value.equals(value)) {
                return elementType;
            }
        }
        throw new IllegalArgumentException("Unsupported value: " + value);
    }
}
