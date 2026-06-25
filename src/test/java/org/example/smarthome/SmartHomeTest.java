package org.example.smarthome;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SmartHomeTest {

    private SmartHome home;
    private SmartLight livingLight;
    private SmartThermostat thermostat;
    private SmartTV bedroomTV;

    @BeforeEach
    void setUp() {
        home = new SmartHome("Test House");
        livingLight = new SmartLight("Living Light", RoomType.LIVING_ROOM);
        thermostat   = new SmartThermostat("Thermostat", RoomType.LIVING_ROOM);
        bedroomTV    = new SmartTV("Bedroom TV", RoomType.BEDROOM);
    }

    // --- addDevice / removeDevice ---

    @Test
    void addDevice_incrementsTotalDevices() {
        home.addDevice(livingLight);
        assertEquals(1, home.getStats().getTotalDevices());
    }

    @Test
    void addMultipleDevices_countsAll() {
        home.addDevice(livingLight);
        home.addDevice(thermostat);
        home.addDevice(bedroomTV);
        assertEquals(3, home.getStats().getTotalDevices());
    }

    @Test
    void removeDevice_decrementsTotalDevices() {
        home.addDevice(livingLight);
        home.addDevice(thermostat);
        home.removeDevice(livingLight);
        assertEquals(1, home.getStats().getTotalDevices());
    }

    @Test
    void removeDevice_notRegistered_doesNothing() {
        home.addDevice(livingLight);
        home.removeDevice(bedroomTV); // never added
        assertEquals(1, home.getStats().getTotalDevices());
    }

    // --- turnOnAll / turnOffAll ---

    @Test
    void turnOnAll_turnsAllDevicesOn() {
        home.addDevice(livingLight);
        home.addDevice(thermostat);
        home.addDevice(bedroomTV);
        home.turnOnAll();
        assertEquals(3, home.countActiveDevices());
    }

    @Test
    void turnOffAll_turnsAllDevicesOff() {
        home.addDevice(livingLight);
        home.addDevice(bedroomTV);
        home.turnOnAll();
        home.turnOffAll();
        assertEquals(0, home.countActiveDevices());
    }

    @Test
    void turnOnAll_recordsTurnOnEvents() {
        home.addDevice(livingLight);
        home.addDevice(thermostat);
        home.turnOnAll();
        assertEquals(2, home.getStats().getTurnOnCount());
    }

    @Test
    void turnOffAll_recordsTurnOffEvents() {
        home.addDevice(livingLight);
        home.addDevice(thermostat);
        home.turnOnAll();
        home.turnOffAll();
        assertEquals(2, home.getStats().getTurnOffCount());
    }

    // --- turnOnByRoom / turnOffByRoom ---

    @Test
    void turnOnByRoom_onlyAffectsTargetRoom() {
        home.addDevice(livingLight);
        home.addDevice(thermostat);
        home.addDevice(bedroomTV);
        home.turnOnByRoom(RoomType.LIVING_ROOM);

        assertTrue(livingLight.isOn());
        assertTrue(thermostat.isOn());
        assertFalse(bedroomTV.isOn());
    }

    @Test
    void turnOffByRoom_onlyAffectsTargetRoom() {
        home.addDevice(livingLight);
        home.addDevice(bedroomTV);
        home.turnOnAll();
        home.turnOffByRoom(RoomType.LIVING_ROOM);

        assertFalse(livingLight.isOn());
        assertTrue(bedroomTV.isOn());
    }

    @Test
    void turnOnByRoom_noMatchingDevices_doesNothing() {
        home.addDevice(bedroomTV);
        home.turnOnByRoom(RoomType.KITCHEN);
        assertEquals(0, home.countActiveDevices());
    }

    // --- getDevicesByRoom ---

    @Test
    void getDevicesByRoom_returnsOnlyMatchingRoom() {
        home.addDevice(livingLight);
        home.addDevice(thermostat);
        home.addDevice(bedroomTV);

        List<SmartDevice> result = home.getDevicesByRoom(RoomType.LIVING_ROOM);
        assertEquals(2, result.size());
        assertTrue(result.contains(livingLight));
        assertTrue(result.contains(thermostat));
        assertFalse(result.contains(bedroomTV));
    }

    @Test
    void getDevicesByRoom_noDevices_returnsEmptyList() {
        home.addDevice(livingLight);
        List<SmartDevice> result = home.getDevicesByRoom(RoomType.KITCHEN);
        assertTrue(result.isEmpty());
    }

    // --- countActiveDevices ---

    @Test
    void countActiveDevices_initiallyZero() {
        home.addDevice(livingLight);
        home.addDevice(bedroomTV);
        assertEquals(0, home.countActiveDevices());
    }

    @Test
    void countActiveDevices_afterPartialTurnOn() {
        home.addDevice(livingLight);
        home.addDevice(bedroomTV);
        livingLight.turnOn();
        assertEquals(1, home.countActiveDevices());
    }

    // --- HomeStats ---

    @Test
    void stats_initialState_allZero() {
        SmartHome.HomeStats stats = home.getStats();
        assertEquals(0, stats.getTotalDevices());
        assertEquals(0, stats.getTurnOnCount());
        assertEquals(0, stats.getTurnOffCount());
    }

    @Test
    void stats_accumulatesAcrossMultipleOperations() {
        home.addDevice(livingLight);
        home.addDevice(bedroomTV);
        home.turnOnAll();  // +2 turnOn
        home.turnOffAll(); // +2 turnOff
        home.turnOnAll();  // +2 turnOn

        assertEquals(4, home.getStats().getTurnOnCount());
        assertEquals(2, home.getStats().getTurnOffCount());
    }

    @Test
    void turnOnAll_alreadyOn_doesNotIncrementTurnOnCount() {
        home.addDevice(livingLight);
        home.turnOnAll(); // OFF → ON: count = 1
        home.turnOnAll(); // already ON: count stays 1
        assertEquals(1, home.getStats().getTurnOnCount());
    }

    @Test
    void turnOffAll_alreadyOff_doesNotIncrementTurnOffCount() {
        home.addDevice(livingLight);
        home.turnOffAll(); // already OFF: count stays 0
        assertEquals(0, home.getStats().getTurnOffCount());
    }

    @Test
    void turnOnByRoom_alreadyOn_doesNotIncrementTurnOnCount() {
        home.addDevice(livingLight);
        home.turnOnByRoom(RoomType.LIVING_ROOM); // OFF → ON: count = 1
        home.turnOnByRoom(RoomType.LIVING_ROOM); // already ON: count stays 1
        assertEquals(1, home.getStats().getTurnOnCount());
    }

    @Test
    void turnOffByRoom_alreadyOff_doesNotIncrementTurnOffCount() {
        home.addDevice(livingLight);
        home.turnOffByRoom(RoomType.LIVING_ROOM); // already OFF: count stays 0
        assertEquals(0, home.getStats().getTurnOffCount());
    }
}
