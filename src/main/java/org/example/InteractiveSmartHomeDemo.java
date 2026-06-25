package org.example;

import org.example.smarthome.*;
import org.example.util.InputHelper;
import org.example.util.InputHelperException;

import java.util.ArrayList;
import java.util.List;

public class InteractiveSmartHomeDemo {

    // toString overridden so readEnum shows "Smart Light" instead of "LIGHT"
    enum DeviceType {
        LIGHT("Smart Light"),
        THERMOSTAT("Smart Thermostat"),
        TV("Smart TV");

        private final String label;
        DeviceType(String label) { this.label = label; }
        @Override public String toString() { return label; }
    }

    private final InputHelper       input;
    private       SmartHome         home;
    private final List<SmartDevice> devices = new ArrayList<>();

    public InteractiveSmartHomeDemo(InputHelper input) {
        this.input = input;
    }

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║   Smart Home — Interactive Demo  ║");
        System.out.println("╚══════════════════════════════════╝");
        System.out.println("(maxRetries=3: каждый ввод даёт 4 попытки)\n");

        try (InputHelper ih = new InputHelper(System.in, System.out, 3)) {
            new InteractiveSmartHomeDemo(ih).run();
        } catch (InputHelperException e) {
            System.err.println("\n[!] Слишком много неверных вводов: " + e.getMessage());
            System.err.println("    Выход.");
        }
    }

    public void run() {
        String address = input.readNonBlank("Адрес дома: ");
        home = new SmartHome(address);
        System.out.println();

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = input.readInt("Выбор: ", 0, 5);
            System.out.println();
            switch (choice) {
                case 1 -> addDevice();
                case 2 -> listDevices();
                case 3 -> controlDevice();
                case 4 -> controlRoom();
                case 5 -> home.getStats().printReport();
                case 0 -> { System.out.println("До свидания!"); running = false; }
            }
        }
    }

    private void printMainMenu() {
        System.out.println("━━━ Главное меню ━━━");
        System.out.println("  1. Добавить устройство");
        System.out.println("  2. Список устройств");
        System.out.println("  3. Управление устройством");
        System.out.println("  4. Управление комнатой");
        System.out.println("  5. Статистика");
        System.out.println("  0. Выход");
    }

    private void addDevice() {
        DeviceType type = input.readEnum("Тип устройства:", DeviceType.class);
        String name     = input.readNonBlank("Название устройства: ");
        RoomType room   = input.readEnum("Комната:", RoomType.class);

        SmartDevice device = switch (type) {
            case LIGHT      -> new SmartLight(name, room);
            case THERMOSTAT -> new SmartThermostat(name, room);
            case TV         -> new SmartTV(name, room);
        };

        if (input.readBoolean("Включить сейчас?")) {
            device.turnOn();
        }

        home.addDevice(device);
        devices.add(device);
        System.out.println();
    }

    private void listDevices() {
        if (devices.isEmpty()) {
            System.out.println("Устройства не зарегистрированы.\n");
            return;
        }
        System.out.println("─── Устройства ───");
        for (int i = 0; i < devices.size(); i++) {
            SmartDevice d = devices.get(i);
            System.out.printf("  %d. %-22s [%-12s]  %-3s  %s%n",
                    i + 1, d.getName(), d.getRoom(),
                    d.isOn() ? "ON" : "off",
                    d.getStatus());
        }
        System.out.printf("Активно: %d/%d%n%n", home.countActiveDevices(), devices.size());
    }

    private void controlDevice() {
        if (devices.isEmpty()) {
            System.out.println("Нет устройств. Сначала добавьте.\n");
            return;
        }
        listDevices();

        int idx = input.readInt("Выберите устройство: ", 1, devices.size()) - 1;
        SmartDevice device = devices.get(idx);

        System.out.printf("%nУстройство: %s [%s]  %s%n",
                device.getName(), device.getRoom(), device.isOn() ? "ON" : "off");

        printDeviceMenu(device);

        int action = input.readInt("Действие: ", 0, 9);
        System.out.println();
        handleDeviceAction(device, action);
    }

    private void printDeviceMenu(SmartDevice device) {
        System.out.println("  1. Включить         2. Выключить");
        if (device instanceof SmartLight) {
            System.out.println("  3. Яркость +        4. Яркость -");
        } else if (device instanceof SmartThermostat) {
            System.out.println("  3. Температура +    4. Температура -");
        } else if (device instanceof SmartTV) {
            System.out.println("  3. Громкость +      4. Громкость -");
            System.out.println("  5. Следующий канал  6. Предыдущий канал");
        }
        System.out.println("  9. Удалить устройство");
        System.out.println("  0. Назад");
    }

    private void handleDeviceAction(SmartDevice device, int action) {
        switch (action) {
            case 0 -> { /* назад */ }
            case 1 -> device.turnOn();
            case 2 -> device.turnOff();
            case 3 -> { if (device instanceof Controllable c) c.increaseValue(); }
            case 4 -> { if (device instanceof Controllable c) c.decreaseValue(); }
            case 5 -> { if (device instanceof SmartTV tv) tv.nextChannel(); }
            case 6 -> { if (device instanceof SmartTV tv) tv.prevChannel(); }
            case 9 -> {
                if (input.readBoolean("Удалить \"" + device.getName() + "\"?")) {
                    home.removeDevice(device);
                    devices.remove(device);
                } else {
                    System.out.println("Отменено.");
                }
            }
            default -> System.out.println("  (нет действия для этого варианта)");
        }
        System.out.println();
    }

    private void controlRoom() {
        RoomType room = input.readEnum("Выберите комнату:", RoomType.class);

        List<SmartDevice> inRoom = home.getDevicesByRoom(room);
        if (inRoom.isEmpty()) {
            System.out.printf("В комнате «%s» нет устройств.%n%n", room);
            return;
        }
        System.out.printf("Устройств в «%s»: %d%n", room, inRoom.size());
        System.out.println("  1. Включить все");
        System.out.println("  2. Выключить все");
        System.out.println("  0. Назад");

        int action = input.readInt("Действие: ", 0, 2);
        System.out.println();
        switch (action) {
            case 1 -> home.turnOnByRoom(room);
            case 2 -> home.turnOffByRoom(room);
        }
        System.out.println();
    }
}
