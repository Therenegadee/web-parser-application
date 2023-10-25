package ru.researchser.models.enums;

public enum TokenType {
    ACTIVATION("ACTIVATION"),
    PASSWORD_RECOVERY("PASSWORD_RECOVERY");

    private final String value;

    TokenType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TokenType fromValue(String value) {
        for (TokenType tokenType : TokenType.values()) {
            if (tokenType.value.equals(value)) {
                return tokenType;
            }
        }
        throw new IllegalArgumentException("Unsupported value: " + value);
    }
}
