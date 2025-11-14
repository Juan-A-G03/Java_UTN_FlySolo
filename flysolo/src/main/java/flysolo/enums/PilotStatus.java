package flysolo.enums;

public enum PilotStatus {
    PENDIENTE("Pending"),
    APROBADO("Approved"),
    RECHAZADO("Rejected");

    private final String displayName;

    PilotStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static PilotStatus fromString(String value) {
        if (value == null) {
            return null;
        }
        for (PilotStatus status : PilotStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid PilotStatus: " + value);
    }

    public static boolean isValid(String value) {
        if (value == null) {
            return false;
        }
        for (PilotStatus status : PilotStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
