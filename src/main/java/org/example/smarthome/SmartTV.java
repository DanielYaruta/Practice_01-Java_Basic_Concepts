package org.example.smarthome;

public class SmartTV extends SmartDevice implements Controllable {

    private static final int MIN_VOLUME = 0;
    private static final int MAX_VOLUME = 100;
    private static final int STEP = 5;

    private int volume;
    private int channel;

    public SmartTV(String name, RoomType room) {
        super(name, room);
        this.volume = 20;
        this.channel = 1;
    }

    @Override
    public void increaseValue() {
        if (!isOn) {
            System.out.println(name + " is OFF. Turn it on first.");
            return;
        }
        if (volume < MAX_VOLUME) {
            volume = Math.min(volume + STEP, MAX_VOLUME);
            System.out.println(name + " volume increased to " + volume);
        } else {
            System.out.println(name + " is already at maximum volume.");
        }
    }

    @Override
    public void decreaseValue() {
        if (!isOn) {
            System.out.println(name + " is OFF. Turn it on first.");
            return;
        }
        if (volume > MIN_VOLUME) {
            volume = Math.max(volume - STEP, MIN_VOLUME);
            System.out.println(name + " volume decreased to " + volume);
        } else {
            System.out.println(name + " is already at minimum volume.");
        }
    }

    public void nextChannel() {
        if (!isOn) {
            System.out.println(name + " is OFF. Turn it on first.");
            return;
        }
        channel++;
        System.out.println(name + " switched to channel " + channel);
    }

    public void prevChannel() {
        if (!isOn) {
            System.out.println(name + " is OFF. Turn it on first.");
            return;
        }
        if (channel > 1) {
            channel--;
            System.out.println(name + " switched to channel " + channel);
        }
    }

    @Override
    public String getStatus() {
        return String.format("Volume: %d | Channel: %d", volume, channel);
    }

    public int getVolume() {
        return volume;
    }

    public int getChannel() {
        return channel;
    }
}
