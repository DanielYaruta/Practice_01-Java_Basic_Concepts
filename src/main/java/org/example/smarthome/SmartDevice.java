package org.example.smarthome;

import org.example.Validate;

public abstract class SmartDevice {

    protected String name;
    protected boolean isOn;
    protected RoomType room;

    public SmartDevice(String name, RoomType room) {
        this.name = Validate.requireNonBlank(name, "name");
        this.room = Validate.requireNonNull(room, "room");
        this.isOn = false;
    }

    public final void turnOn() {
        if (!isOn) {
            isOn = true;
            System.out.println(name + " in " + room + " turned ON.");
        } else {
            System.out.println(name + " is already ON.");
        }
    }

    public final void turnOff() {
        if (isOn) {
            isOn = false;
            System.out.println(name + " in " + room + " turned OFF.");
        } else {
            System.out.println(name + " is already OFF.");
        }
    }

    public final boolean isOn() {
        return isOn;
    }

    public final String getName() {
        return name;
    }

    public final RoomType getRoom() {
        return room;
    }

    public abstract String getStatus();

    @Override
    public String toString() {
        return String.format("[%s] %s (%s) - %s", room, name, getClass().getSimpleName(), isOn ? "ON" : "OFF");
    }
}
