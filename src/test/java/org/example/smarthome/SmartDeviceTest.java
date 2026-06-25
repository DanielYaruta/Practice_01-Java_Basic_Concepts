package org.example.smarthome;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SmartDeviceTest {

    // SmartDevice is abstract — tested through SmartLight as the simplest concrete subclass
    private SmartLight device;

    @BeforeEach
    void setUp() {
        device = new SmartLight("Test Light", RoomType.BEDROOM);
    }

    // --- Initial state ---

    @Test
    void newDevice_isOff() {
        assertFalse(device.isOn());
    }

    @Test
    void newDevice_hasCorrectName() {
        assertEquals("Test Light", device.getName());
    }

    @Test
    void newDevice_hasCorrectRoom() {
        assertEquals(RoomType.BEDROOM, device.getRoom());
    }

    // --- turnOn ---

    @Test
    void turnOn_setsDeviceOn() {
        device.turnOn();
        assertTrue(device.isOn());
    }

    @Test
    void turnOn_whenAlreadyOn_remainsOn() {
        device.turnOn();
        device.turnOn();
        assertTrue(device.isOn());
    }

    // --- turnOff ---

    @Test
    void turnOff_setsDeviceOff() {
        device.turnOn();
        device.turnOff();
        assertFalse(device.isOn());
    }

    @Test
    void turnOff_whenAlreadyOff_remainsOff() {
        device.turnOff();
        assertFalse(device.isOn());
    }

    // --- toggle sequence ---

    @Test
    void toggleOnThenOff_returnsToOffState() {
        device.turnOn();
        device.turnOff();
        assertFalse(device.isOn());
    }

    // --- toString ---

    @Test
    void toString_containsNameAndRoom() {
        String s = device.toString();
        assertTrue(s.contains("Test Light"));
        assertTrue(s.contains("Bedroom"));
    }

    @Test
    void toString_reflectsOnState() {
        device.turnOn();
        assertTrue(device.toString().contains("ON"));
    }

    @Test
    void toString_reflectsOffState() {
        assertTrue(device.toString().contains("OFF"));
    }
}
