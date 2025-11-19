package flysolo.enums;

public enum Faction {
    IMPERIAL("Imperial"),
    REBEL("Rebel"),
    NEUTRAL("Neutral");

    private final String displayName;

    Faction(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Faction fromString(String value) {
        if (value == null) {
            return null;
        }
        for (Faction faction : Faction.values()) {
            if (faction.name().equalsIgnoreCase(value)) {
                return faction;
            }
        }
        throw new IllegalArgumentException("Invalid Faction: " + value);
    }

    public static boolean isValid(String value) {
        if (value == null) {
            return false;
        }
        for (Faction faction : Faction.values()) {
            if (faction.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
