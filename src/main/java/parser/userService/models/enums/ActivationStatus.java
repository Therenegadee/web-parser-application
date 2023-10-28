package parser.userService.models.enums;

public enum ActivationStatus {
    VERIFIED("VERIFIED"),
    WAIT_FOR_EMAIL_VERIFICATION("WAIT_FOR_EMAIL_VERIFICATION");

    private final String value;

    ActivationStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ActivationStatus fromValue(String value) {
        for (ActivationStatus activationStatus : ActivationStatus.values()) {
            if (activationStatus.value.equals(value)) {
                return activationStatus;
            }
        }
        throw new IllegalArgumentException("Unsupported value: " + value);
    }
}
