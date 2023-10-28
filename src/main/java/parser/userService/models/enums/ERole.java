package parser.userService.models.enums;

public enum ERole {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String value;

    ERole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ERole fromValue(String value) {
        for (ERole eRole : ERole.values()) {
            if (eRole.value.equals(value)) {
                return eRole;
            }
        }
        throw new IllegalArgumentException("Unsupported value: " + value);
    }
}
