package flysolo.enums;

public enum ShipStatus {
    PENDIENTE("Pending"),
    APROBADO("Approved"),
    RECHAZADO("Rejected");

    private final String displayName;

    ShipStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ShipStatus fromString(String value) {
        if (value == null) {
            return null;
        }
        for (ShipStatus status : ShipStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid ShipStatus: " + value);
    }

    public static boolean isValid(String value) {
        if (value == null) {
            return false;
        }
        for (ShipStatus status : ShipStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
