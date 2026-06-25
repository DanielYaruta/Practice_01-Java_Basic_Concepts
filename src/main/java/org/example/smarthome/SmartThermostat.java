package org.example.smarthome;

public class SmartThermostat extends SmartDevice implements Controllable {

    private static final double MIN_TEMP = 15.0;
    private static final double MAX_TEMP = 30.0;
    private static final double STEP = 0.5;

    private double temperature;

    public SmartThermostat(String name, RoomType room) {
        super(name, room);
        this.temperature = 22.0;
    }

    @Override
    public void increaseValue() {
        if (!isOn) {
            System.out.println(name + " is OFF. Turn it on first.");
            return;
        }
        if (temperature < MAX_TEMP) {
            temperature = Math.min(temperature + STEP, MAX_TEMP);
            System.out.printf("%s temperature increased to %.1f°C%n", name, temperature);
        } else {
            System.out.println(name + " is already at maximum temperature.");
        }
    }

    @Override
    public void decreaseValue() {
        if (!isOn) {
            System.out.println(name + " is OFF. Turn it on first.");
            return;
        }
        if (temperature > MIN_TEMP) {
            temperature = Math.max(temperature - STEP, MIN_TEMP);
            System.out.printf("%s temperature decreased to %.1f°C%n", name, temperature);
        } else {
            System.out.println(name + " is already at minimum temperature.");
        }
    }

    @Override
    public String getStatus() {
        return String.format("Temperature: %.1f°C", temperature);
    }

    public double getTemperature() {
        return temperature;
    }
}
