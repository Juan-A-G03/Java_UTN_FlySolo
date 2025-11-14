package flysolo.enums;

public enum UserType {
    PASSENGER("Passenger"),
    PILOT("Pilot"),
    ADMIN("Admin");

    private final String displayName;

    UserType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static UserType fromString(String value) {
        if (value == null) {
            return null;
        }
        for (UserType type : UserType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid UserType: " + value);
    }

    public static boolean isValid(String value) {
        if (value == null) {
            return false;
        }
        for (UserType type : UserType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
