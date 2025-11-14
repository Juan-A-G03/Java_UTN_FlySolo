package flysolo.enums;

public enum PaymentMethod {
    TRANSFERENCIA("Bank Transfer"),
    CREDITO("Credit Card");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static PaymentMethod fromString(String value) {
        if (value == null) {
            return null;
        }
        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.name().equalsIgnoreCase(value)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Invalid PaymentMethod: " + value);
    }

    public static boolean isValid(String value) {
        if (value == null) {
            return false;
        }
        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
