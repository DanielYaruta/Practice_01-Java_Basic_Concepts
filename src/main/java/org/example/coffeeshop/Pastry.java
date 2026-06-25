package org.example.coffeeshop;

public class Pastry extends MenuItem {

    private final boolean glutenFree;
    private final boolean servedWarm;

    public Pastry(String name, double price, boolean glutenFree, boolean servedWarm) {
        super(name, price);
        this.glutenFree  = glutenFree;
        this.servedWarm  = servedWarm;
    }

    @Override
    public String getDescription() {
        String tags = (glutenFree ? "gluten-free" : "regular")
                + (servedWarm ? ", warm" : "");
        return String.format("$%.2f  [Pastry | %s]", price, tags);
    }

    public boolean isGlutenFree() { return glutenFree; }
    public boolean isServedWarm() { return servedWarm; }
}
