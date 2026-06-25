package org.example.smarthome;

class SmartLightControllableTest extends ControllableContractTest {

    @Override
    protected Controllable createActive() {
        SmartLight light = new SmartLight("Light", RoomType.BEDROOM);
        light.turnOn(); // default brightness 50% — room to increase and decrease
        return light;
    }

    @Override
    protected Controllable createInactive() {
        return new SmartLight("Light", RoomType.BEDROOM); // starts OFF
    }
}
