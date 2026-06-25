package org.example.smarthome;

public class SmartLight extends SmartDevice implements Controllable {

    private static final int MIN_BRIGHTNESS = 0;
    private static final int MAX_BRIGHTNESS = 100;
    private static final int STEP = 10;

    private int brightness;

    public SmartLight(String name, RoomType room) {
        super(name, room);
        this.brightness = 50;
    }

    @Override
    public void increaseValue() {
        if (!isOn) {
            System.out.println(name + " is OFF. Turn it on first.");
            return;
        }
        if (brightness < MAX_BRIGHTNESS) {
            brightness = Math.min(brightness + STEP, MAX_BRIGHTNESS);
            System.out.println(name + " brightness increased to " + brightness + "%");
        } else {
            System.out.println(name + " is already at maximum brightness.");
        }
    }

    @Override
    public void decreaseValue() {
        if (!isOn) {
            System.out.println(name + " is OFF. Turn it on first.");
            return;
        }
        if (brightness > MIN_BRIGHTNESS) {
            brightness = Math.max(brightness - STEP, MIN_BRIGHTNESS);
            System.out.println(name + " brightness decreased to " + brightness + "%");
        } else {
            System.out.println(name + " is already at minimum brightness.");
        }
    }

    @Override
    public String getStatus() {
        return String.format("Brightness: %d%%", brightness);
    }

    public int getBrightness() {
        return brightness;
    }
}
