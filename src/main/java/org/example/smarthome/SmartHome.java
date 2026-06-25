package org.example.smarthome;

import org.example.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SmartHome {

    private final String address;
    private final List<SmartDevice> devices;
    private final HomeStats stats;

    public SmartHome(String address) {
        this.address = Validate.requireNonBlank(address, "address");
        this.devices = new ArrayList<>();
        this.stats = new HomeStats();
    }

    public void addDevice(SmartDevice device) {
        Validate.requireNonNull(device, "device");
        devices.add(device);
        stats.totalDevices++;
        System.out.println("Device added: " + device.getName());
    }

    public void removeDevice(SmartDevice device) {
        Validate.requireNonNull(device, "device");
        if (devices.remove(device)) {
            stats.totalDevices--;
            System.out.println("Device removed: " + device.getName());
        }
    }

    public void turnOnAll() {
        System.out.println("--- Turning ON all devices ---");
        for (SmartDevice device : devices) {
            device.turnOn();
            stats.turnOnCount++;
        }
    }

    public void turnOffAll() {
        System.out.println("--- Turning OFF all devices ---");
        for (SmartDevice device : devices) {
            device.turnOff();
            stats.turnOffCount++;
        }
    }

    public void turnOnByRoom(RoomType room) {
        Validate.requireNonNull(room, "room");
        System.out.println("--- Turning ON all devices in " + room + " ---");
        devices.stream()
                .filter(d -> d.getRoom() == room)
                .forEach(d -> {
                    d.turnOn();
                    stats.turnOnCount++;
                });
    }

    public void turnOffByRoom(RoomType room) {
        Validate.requireNonNull(room, "room");
        System.out.println("--- Turning OFF all devices in " + room + " ---");
        devices.stream()
                .filter(d -> d.getRoom() == room)
                .forEach(d -> {
                    d.turnOff();
                    stats.turnOffCount++;
                });
    }

    public List<SmartDevice> getDevicesByRoom(RoomType room) {
        Validate.requireNonNull(room, "room");
        return devices.stream()
                .filter(d -> d.getRoom() == room)
                .collect(Collectors.toList());
    }

    public int countActiveDevices() {
        return (int) devices.stream().filter(SmartDevice::isOn).count();
    }

    public void printAllStatus() {
        System.out.println("=== Smart Home: " + address + " ===");
        if (devices.isEmpty()) {
            System.out.println("No devices registered.");
            return;
        }
        for (SmartDevice device : devices) {
            System.out.printf("  %-40s %s%n", device, device.getStatus());
        }
        System.out.println("Active devices: " + countActiveDevices() + "/" + devices.size());
    }

    public HomeStats getStats() {
        return stats;
    }

    // --- Nested static class ---

    public static class HomeStats {

        private int totalDevices;
        private int turnOnCount;
        private int turnOffCount;

        private HomeStats() {}

        public int getTotalDevices() {
            return totalDevices;
        }

        public int getTurnOnCount() {
            return turnOnCount;
        }

        public int getTurnOffCount() {
            return turnOffCount;
        }

        public void printReport() {
            System.out.println("=== Home Usage Statistics ===");
            System.out.println("  Total devices registered : " + totalDevices);
            System.out.println("  Total turn-on  events    : " + turnOnCount);
            System.out.println("  Total turn-off events    : " + turnOffCount);
        }
    }
}
