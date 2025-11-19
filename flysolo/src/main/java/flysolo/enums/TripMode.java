package flysolo.enums;

public enum TripMode {
    NORMAL("Normal"),
    UNDERCOVER("Undercover");

    private final String displayName;

    TripMode(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static TripMode fromString(String value) {
        if (value == null) {
            return null;
        }
        for (TripMode mode : TripMode.values()) {
            if (mode.name().equalsIgnoreCase(value)) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Invalid TripMode: " + value);
    }

    public static boolean isValid(String value) {
        if (value == null) {
            return false;
        }
        for (TripMode mode : TripMode.values()) {
            if (mode.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
