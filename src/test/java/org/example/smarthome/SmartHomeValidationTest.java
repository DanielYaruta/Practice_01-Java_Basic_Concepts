package org.example.smarthome;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class SmartHomeValidationTest {

    private final PrintStream originalOut = System.out;

    @BeforeEach
    void suppressOutput() { System.setOut(new PrintStream(new ByteArrayOutputStream())); }

    @AfterEach
    void restoreOutput() { System.setOut(originalOut); }

    // --- constructor ---

    @Test
    void constructor_nullAddress_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new SmartHome(null));
    }

    @Test
    void constructor_blankAddress_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new SmartHome("  "));
    }

    @Test
    void constructor_validAddress_doesNotThrow() {
        assertDoesNotThrow(() -> new SmartHome("123 Main St"));
    }

    // --- addDevice ---

    @Test
    void addDevice_nullDevice_throwsNullPointerException() {
        SmartHome home = new SmartHome("Home");
        assertThrows(NullPointerException.class, () -> home.addDevice(null));
    }

    // --- removeDevice ---

    @Test
    void removeDevice_nullDevice_throwsNullPointerException() {
        SmartHome home = new SmartHome("Home");
        assertThrows(NullPointerException.class, () -> home.removeDevice(null));
    }

    // --- turnOnByRoom / turnOffByRoom ---

    @Test
    void turnOnByRoom_nullRoom_throwsNullPointerException() {
        SmartHome home = new SmartHome("Home");
        assertThrows(NullPointerException.class, () -> home.turnOnByRoom(null));
    }

    @Test
    void turnOffByRoom_nullRoom_throwsNullPointerException() {
        SmartHome home = new SmartHome("Home");
        assertThrows(NullPointerException.class, () -> home.turnOffByRoom(null));
    }

    // --- getDevicesByRoom ---

    @Test
    void getDevicesByRoom_nullRoom_throwsNullPointerException() {
        SmartHome home = new SmartHome("Home");
        assertThrows(NullPointerException.class, () -> home.getDevicesByRoom(null));
    }
}
