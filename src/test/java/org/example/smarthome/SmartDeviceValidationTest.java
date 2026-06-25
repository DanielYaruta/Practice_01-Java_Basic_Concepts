package org.example.smarthome;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SmartDeviceValidationTest {

    // SmartDevice is abstract — tested through SmartLight

    @Test
    void constructor_nullName_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> new SmartLight(null, RoomType.BEDROOM));
    }

    @Test
    void constructor_blankName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new SmartLight("   ", RoomType.BEDROOM));
    }

    @Test
    void constructor_emptyName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new SmartLight("", RoomType.BEDROOM));
    }

    @Test
    void constructor_nullRoom_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> new SmartLight("Lamp", null));
    }

    @Test
    void constructor_validArguments_doesNotThrow() {
        assertDoesNotThrow(() -> new SmartLight("Lamp", RoomType.BEDROOM));
    }
}
