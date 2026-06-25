package org.example.smarthome;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class RoomTypeTest {

    // --- All values present ---

    @Test
    void enumHasSixValues() {
        assertEquals(6, RoomType.values().length);
    }

    @Test
    void allExpectedValuesExist() {
        assertDoesNotThrow(() -> {
            RoomType.valueOf("LIVING_ROOM");
            RoomType.valueOf("BEDROOM");
            RoomType.valueOf("KITCHEN");
            RoomType.valueOf("BATHROOM");
            RoomType.valueOf("OFFICE");
            RoomType.valueOf("GARAGE");
        });
    }

    // --- getDisplayName ---

    @Test
    void livingRoom_displayName() {
        assertEquals("Living Room", RoomType.LIVING_ROOM.getDisplayName());
    }

    @Test
    void bedroom_displayName() {
        assertEquals("Bedroom", RoomType.BEDROOM.getDisplayName());
    }

    @Test
    void kitchen_displayName() {
        assertEquals("Kitchen", RoomType.KITCHEN.getDisplayName());
    }

    @Test
    void bathroom_displayName() {
        assertEquals("Bathroom", RoomType.BATHROOM.getDisplayName());
    }

    @Test
    void office_displayName() {
        assertEquals("Office", RoomType.OFFICE.getDisplayName());
    }

    @Test
    void garage_displayName() {
        assertEquals("Garage", RoomType.GARAGE.getDisplayName());
    }

    // --- toString matches getDisplayName ---

    @ParameterizedTest
    @EnumSource(RoomType.class)
    void toString_equalsDisplayName(RoomType room) {
        assertEquals(room.getDisplayName(), room.toString());
    }

    // --- getDisplayName contract ---

    @ParameterizedTest
    @EnumSource(RoomType.class)
    void getDisplayName_neverNullOrBlank(RoomType room) {
        assertNotNull(room.getDisplayName());
        assertFalse(room.getDisplayName().isBlank());
    }

    @ParameterizedTest
    @EnumSource(RoomType.class)
    void getDisplayName_doesNotEqualEnumName(RoomType room) {
        assertNotEquals(room.name(), room.getDisplayName());
    }

    // --- valueOf / identity ---

    @Test
    void valueOf_returnsCorrectConstant() {
        assertSame(RoomType.BEDROOM, RoomType.valueOf("BEDROOM"));
    }

    @Test
    void valueOf_unknownName_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> RoomType.valueOf("UNKNOWN"));
    }

    @ParameterizedTest
    @EnumSource(RoomType.class)
    void sameConstant_isEqualToItself(RoomType room) {
        assertSame(room, RoomType.valueOf(room.name()));
    }
}
