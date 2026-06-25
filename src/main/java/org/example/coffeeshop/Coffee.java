package org.example.coffeeshop;

import org.example.Validate;

public class Coffee extends MenuItem implements Preparable {

    public enum Strength {
        LIGHT("Light"),
        MEDIUM("Medium"),
        STRONG("Strong");

        private final String displayName;

        Strength(String displayName) { this.displayName = displayName; }

        @Override
        public String toString() { return displayName; }
    }

    private final Strength strength;
    private final Size size;

    public Coffee(String name, double basePrice, Strength strength, Size size) {
        super(name, computePrice(basePrice, size));
        this.strength = Validate.requireNonNull(strength, "strength");
        this.size     = size; // validated in computePrice
    }

    @Override
    public void prepare() {
        System.out.println("Preparing " + size + " " + name + "...");
        System.out.println("  > Grinding " + strength + " coffee beans...");
        System.out.println("  > Brewing espresso...");
        if (size != Size.SMALL) {
            System.out.println("  > Adding steamed milk for " + size + " size...");
        }
        System.out.println("  " + size + " " + name + " is ready!");
    }

    @Override
    public String getDescription() {
        return String.format("$%.2f  [Coffee | %s | %s]", price, strength, size);
    }

    public Strength getStrength() { return strength; }
    public Size getSize()         { return size; }
}
