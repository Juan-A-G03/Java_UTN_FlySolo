package flysolo.enums;

public enum PaymentStatus {
    PENDIENTE("Pending"),
    PAGADO("Paid"),
    FALLIDO("Failed"),
    REEMBOLSADO("Refunded");

    private final String displayName;

    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static PaymentStatus fromString(String value) {
        if (value == null) {
            return null;
        }
        for (PaymentStatus status : PaymentStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid PaymentStatus: " + value);
    }

    public static boolean isValid(String value) {
        if (value == null) {
            return false;
        }
        for (PaymentStatus status : PaymentStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
