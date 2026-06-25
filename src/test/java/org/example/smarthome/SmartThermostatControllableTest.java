package org.example.smarthome;

class SmartThermostatControllableTest extends ControllableContractTest {

    @Override
    protected Controllable createActive() {
        SmartThermostat t = new SmartThermostat("Thermostat", RoomType.LIVING_ROOM);
        t.turnOn(); // default 22°C — room to increase (max 30) and decrease (min 15)
        return t;
    }

    @Override
    protected Controllable createInactive() {
        return new SmartThermostat("Thermostat", RoomType.LIVING_ROOM); // starts OFF
    }
}
