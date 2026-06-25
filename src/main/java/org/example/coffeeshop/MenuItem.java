package org.example.coffeeshop;

import org.example.Validate;

public abstract class MenuItem {

    protected String name;
    protected double price;

    public MenuItem(String name, double price) {
        this.name  = Validate.requireNonBlank(name, "name");
        this.price = Validate.requireNonNegative(price, "price");
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public abstract String getDescription();

    @Override
    public String toString() {
        return String.format("%-22s %s", name, getDescription());
    }
}
