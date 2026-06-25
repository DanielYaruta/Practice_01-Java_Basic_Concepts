package org.example.smarthome;

public enum RoomType {
    LIVING_ROOM("Living Room"),
    BEDROOM("Bedroom"),
    KITCHEN("Kitchen"),
    BATHROOM("Bathroom"),
    OFFICE("Office"),
    GARAGE("Garage");

    private final String displayName;

    RoomType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
