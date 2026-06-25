package org.example;

import org.example.coffeeshop.CoffeeShopDemo;
import org.example.smarthome.SmartHomeDemo;
import org.example.taskmanager.TaskManagementDemo;

public class Main {
    public static void main(String[] args) {
        SmartHomeDemo.run();
        System.out.println("\n" + "=".repeat(50) + "\n");
        CoffeeShopDemo.run();
        System.out.println("\n" + "=".repeat(50) + "\n");
        TaskManagementDemo.run();
    }
}
