package org.example.coffeeshop;

import org.example.Validate;

public class Tea extends MenuItem implements Preparable {

    public enum TeaType {
        GREEN("Green",   75, 3),
        BLACK("Black",   95, 4),
        HERBAL("Herbal", 85, 5),
        OOLONG("Oolong", 80, 4);

        private final String displayName;
        private final int waterTempCelsius;
        private final int steepingMinutes;

        TeaType(String displayName, int waterTempCelsius, int steepingMinutes) {
            this.displayName      = displayName;
            this.waterTempCelsius = waterTempCelsius;
            this.steepingMinutes  = steepingMinutes;
        }

        public int getWaterTempCelsius() { return waterTempCelsius; }
        public int getSteepingMinutes()  { return steepingMinutes; }

        @Override
        public String toString() { return displayName; }
    }

    private final TeaType type;
    private final Size size;

    public Tea(String name, double basePrice, TeaType type, Size size) {
        super(name, computePrice(basePrice, size));
        this.type = Validate.requireNonNull(type, "type");
        this.size = size; // validated in computePrice
    }

    private static double computePrice(double basePrice, Size size) {
        Validate.requireNonNegative(basePrice, "basePrice");
        return basePrice * Validate.requireNonNull(size, "size").getPriceMultiplier();
    }

    @Override
    public void prepare() {
        System.out.println("Preparing " + size + " " + name + "...");
        System.out.printf("  > Heating water to %d°C...%n", type.getWaterTempCelsius());
        System.out.printf("  > Steeping %s tea for %d minutes...%n", type, type.getSteepingMinutes());
        System.out.println("  " + size + " " + name + " is ready!");
    }

    @Override
    public String getDescription() {
        return String.format("$%.2f  [Tea | %s | %s | steep %d min]",
                price, type, size, type.getSteepingMinutes());
    }

    public TeaType getType() { return type; }
    public Size getSize()    { return size; }
}
