package org.example.smarthome;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SmartThermostatTest {

    private SmartThermostat thermostat;

    @BeforeEach
    void setUp() {
        thermostat = new SmartThermostat("Thermostat", RoomType.LIVING_ROOM);
        thermostat.turnOn();
    }

    // --- Initial state ---

    @Test
    void initialTemperature_is22() {
        SmartThermostat fresh = new SmartThermostat("New", RoomType.OFFICE);
        assertEquals(22.0, fresh.getTemperature(), 0.001);
    }

    // --- increaseValue ---

    @Test
    void increaseValue_addsHalfDegree() {
        thermostat.increaseValue();
        assertEquals(22.5, thermostat.getTemperature(), 0.001);
    }

    @Test
    void increaseValue_multipleSteps() {
        thermostat.increaseValue();
        thermostat.increaseValue();
        assertEquals(23.0, thermostat.getTemperature(), 0.001);
    }

    @Test
    void increaseValue_atMax_staysAt30() {
        for (int i = 0; i < 20; i++) thermostat.increaseValue();
        assertEquals(30.0, thermostat.getTemperature(), 0.001);
    }

    @Test
    void increaseValue_whenOff_doesNotChange() {
        thermostat.turnOff();
        thermostat.increaseValue();
        assertEquals(22.0, thermostat.getTemperature(), 0.001);
    }

    // --- decreaseValue ---

    @Test
    void decreaseValue_subtractsHalfDegree() {
        thermostat.decreaseValue();
        assertEquals(21.5, thermostat.getTemperature(), 0.001);
    }

    @Test
    void decreaseValue_atMin_staysAt15() {
        for (int i = 0; i < 20; i++) thermostat.decreaseValue();
        assertEquals(15.0, thermostat.getTemperature(), 0.001);
    }

    @Test
    void decreaseValue_whenOff_doesNotChange() {
        thermostat.turnOff();
        thermostat.decreaseValue();
        assertEquals(22.0, thermostat.getTemperature(), 0.001);
    }

    // --- boundary ---

    @Test
    void increaseValue_doesNotExceedMax() {
        for (int i = 0; i < 16; i++) thermostat.increaseValue(); // 22 + 8 = 30
        assertEquals(30.0, thermostat.getTemperature(), 0.001);
        thermostat.increaseValue();
        assertEquals(30.0, thermostat.getTemperature(), 0.001);
    }

    @Test
    void decreaseValue_doesNotGoBelowMin() {
        for (int i = 0; i < 14; i++) thermostat.decreaseValue(); // 22 - 7 = 15
        assertEquals(15.0, thermostat.getTemperature(), 0.001);
        thermostat.decreaseValue();
        assertEquals(15.0, thermostat.getTemperature(), 0.001);
    }

    // --- getStatus ---

    @Test
    void getStatus_containsTemperature() {
        assertTrue(thermostat.getStatus().contains("22"));
    }

    @Test
    void getStatus_updatesAfterChange() {
        thermostat.increaseValue();
        assertTrue(thermostat.getStatus().contains("22.5") || thermostat.getStatus().contains("22,5"));
    }
}
