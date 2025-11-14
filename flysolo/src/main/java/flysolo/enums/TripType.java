package flysolo.enums;

public enum TripType {
    INMEDIATO("Immediate"),
    PROGRAMADO("Scheduled");

    private final String displayName;

    TripType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static TripType fromString(String value) {
        if (value == null) {
            return null;
        }
        for (TripType type : TripType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid TripType: " + value);
    }

    public static boolean isValid(String value) {
        if (value == null) {
            return false;
        }
        for (TripType type : TripType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
