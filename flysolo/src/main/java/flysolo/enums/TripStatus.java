package flysolo.enums;

public enum TripStatus {
    PENDIENTE("Pending"),
    CONFIRMADO("Confirmed"),
    EN_CURSO("In Progress"),
    COMPLETADO("Completed"),
    CANCELADO("Canceled");

    private final String displayName;

    TripStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static TripStatus fromString(String value) {
        if (value == null) {
            return null;
        }
        for (TripStatus status : TripStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid TripStatus: " + value);
    }

    public static boolean isValid(String value) {
        if (value == null) {
            return false;
        }
        for (TripStatus status : TripStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
