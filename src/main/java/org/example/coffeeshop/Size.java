package org.example.coffeeshop;

public enum Size {
    SMALL("Small",   0.8),
    MEDIUM("Medium", 1.0),
    LARGE("Large",   1.3);

    private final String displayName;
    private final double priceMultiplier;

    Size(String displayName, double priceMultiplier) {
        this.displayName = displayName;
        this.priceMultiplier = priceMultiplier;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getPriceMultiplier() {
        return priceMultiplier;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
