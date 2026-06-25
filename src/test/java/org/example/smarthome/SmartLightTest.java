package org.example.smarthome;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SmartLightTest {

    private SmartLight light;

    @BeforeEach
    void setUp() {
        light = new SmartLight("Lamp", RoomType.BEDROOM);
        light.turnOn();
    }

    // --- Initial state ---

    @Test
    void initialBrightness_is50() {
        SmartLight fresh = new SmartLight("New", RoomType.KITCHEN);
        assertEquals(50, fresh.getBrightness());
    }

    // --- increaseValue ---

    @Test
    void increaseValue_addsTenPercent() {
        light.increaseValue();
        assertEquals(60, light.getBrightness());
    }

    @Test
    void increaseValue_multipleSteps() {
        light.increaseValue();
        light.increaseValue();
        light.increaseValue();
        assertEquals(80, light.getBrightness());
    }

    @Test
    void increaseValue_atMax_staysAt100() {
        for (int i = 0; i < 10; i++) light.increaseValue();
        assertEquals(100, light.getBrightness());
    }

    @Test
    void increaseValue_whenOff_doesNotChange() {
        light.turnOff();
        light.increaseValue();
        assertEquals(50, light.getBrightness());
    }

    // --- decreaseValue ---

    @Test
    void decreaseValue_subtractsTenPercent() {
        light.decreaseValue();
        assertEquals(40, light.getBrightness());
    }

    @Test
    void decreaseValue_atMin_staysAt0() {
        for (int i = 0; i < 10; i++) light.decreaseValue();
        assertEquals(0, light.getBrightness());
    }

    @Test
    void decreaseValue_whenOff_doesNotChange() {
        light.turnOff();
        light.decreaseValue();
        assertEquals(50, light.getBrightness());
    }

    // --- boundary: step reaches exact max/min ---

    @Test
    void increaseValue_doesNotExceedMax() {
        // start at 50, 5 increases = 100
        for (int i = 0; i < 5; i++) light.increaseValue();
        assertEquals(100, light.getBrightness());
        light.increaseValue(); // one more — must stay at 100
        assertEquals(100, light.getBrightness());
    }

    @Test
    void decreaseValue_doesNotGoBelowMin() {
        for (int i = 0; i < 5; i++) light.decreaseValue();
        assertEquals(0, light.getBrightness());
        light.decreaseValue();
        assertEquals(0, light.getBrightness());
    }

    // --- getStatus ---

    @Test
    void getStatus_containsBrightness() {
        assertTrue(light.getStatus().contains("50%"));
    }

    @Test
    void getStatus_updatesAfterChange() {
        light.increaseValue();
        assertTrue(light.getStatus().contains("60%"));
    }
}
