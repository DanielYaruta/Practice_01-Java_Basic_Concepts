package org.example.smarthome;

public class SmartHomeDemo {

    public static void run() {
        SmartHome home = new SmartHome("123 Main Street");

        // Create devices
        SmartLight livingRoomLight = new SmartLight("Ceiling Light", RoomType.LIVING_ROOM);
        SmartLight bedroomLight    = new SmartLight("Bedside Lamp", RoomType.BEDROOM);
        SmartThermostat thermostat = new SmartThermostat("Main Thermostat", RoomType.LIVING_ROOM);
        SmartTV livingRoomTV       = new SmartTV("Samsung TV", RoomType.LIVING_ROOM);
        SmartTV bedroomTV          = new SmartTV("Bedroom TV", RoomType.BEDROOM);

        // Register devices
        home.addDevice(livingRoomLight);
        home.addDevice(bedroomLight);
        home.addDevice(thermostat);
        home.addDevice(livingRoomTV);
        home.addDevice(bedroomTV);

        System.out.println();
        home.printAllStatus();

        // Turn on all living room devices
        System.out.println();
        home.turnOnByRoom(RoomType.LIVING_ROOM);

        // Control individual devices
        System.out.println();
        System.out.println("--- Adjusting Living Room Light ---");
        livingRoomLight.increaseValue();
        livingRoomLight.increaseValue();
        livingRoomLight.increaseValue();
        livingRoomLight.decreaseValue();

        System.out.println();
        System.out.println("--- Adjusting Thermostat ---");
        thermostat.increaseValue();
        thermostat.increaseValue();

        System.out.println();
        System.out.println("--- Adjusting TV ---");
        livingRoomTV.increaseValue();
        livingRoomTV.nextChannel();
        livingRoomTV.nextChannel();

        // Turn on bedroom
        System.out.println();
        home.turnOnByRoom(RoomType.BEDROOM);

        // Full status report
        System.out.println();
        home.printAllStatus();

        // Turn off living room
        System.out.println();
        home.turnOffByRoom(RoomType.LIVING_ROOM);

        // Final status + stats
        System.out.println();
        home.printAllStatus();

        System.out.println();
        home.getStats().printReport();
    }
}
