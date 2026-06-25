package org.example.smarthome;

class SmartTVControllableTest extends ControllableContractTest {

    @Override
    protected Controllable createActive() {
        SmartTV tv = new SmartTV("TV", RoomType.LIVING_ROOM);
        tv.turnOn(); // default volume 20 — room to increase (max 100) and decrease (min 0)
        return tv;
    }

    @Override
    protected Controllable createInactive() {
        return new SmartTV("TV", RoomType.LIVING_ROOM); // starts OFF
    }
}
