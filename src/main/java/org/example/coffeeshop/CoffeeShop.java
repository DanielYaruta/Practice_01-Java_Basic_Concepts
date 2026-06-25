package org.example.coffeeshop;

import org.example.Validate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CoffeeShop {

    private static final DateTimeFormatter TIME_FMT =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    private final String name;
    private final List<MenuItem> menu;
    private final List<Order> orders;
    private final CoffeeShopStats stats;

    public CoffeeShop(String name) {
        this.name   = Validate.requireNonBlank(name, "name");
        this.menu   = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.stats  = new CoffeeShopStats();
    }

    public void addToMenu(MenuItem item) {
        Validate.requireNonNull(item, "item");
        menu.add(item);
    }

    public Order createOrder(String customerName) {
        Validate.requireNonBlank(customerName, "customerName");
        return new Order(customerName);
    }

    public void placeOrder(Order order) {
        Validate.requireNonNull(order, "order");
        orders.add(order);
        stats.recordOrder(order);

        System.out.println("=== Order for " + order.getCustomerName()
                + " [" + order.getOrderTime().format(TIME_FMT) + "] ===");

        for (MenuItem item : order.getItems()) {
            if (item instanceof Preparable preparable) {
                preparable.prepare();
            } else {
                System.out.println("  Serving " + item.getName() + "...");
            }
        }

        System.out.printf("  Total: $%.2f%n", order.getTotal());
    }

    public void printMenu() {
        System.out.println("=== Menu: " + name + " ===");
        for (MenuItem item : menu) {
            System.out.println("  " + item);
        }
    }

    public CoffeeShopStats getStats() {
        return stats;
    }

    public String getName() {
        return name;
    }

    List<Order> getOrders() {
        return orders;
    }

    List<MenuItem> getMenu() {
        return menu;
    }

    // -------------------------------------------------------------------------
    // Nested class — has access to the enclosing CoffeeShop instance
    // -------------------------------------------------------------------------

    public class Order {

        private final String customerName;
        private final List<MenuItem> items;
        private final LocalDateTime orderTime;

        public Order(String customerName) {
            this.customerName = customerName;
            this.items        = new ArrayList<>();
            this.orderTime    = LocalDateTime.now();
        }

        public Order addItem(MenuItem item) {
            Validate.requireNonNull(item, "item");
            items.add(item);
            return this;
        }

        public double getTotal() {
            return items.stream().mapToDouble(MenuItem::getPrice).sum();
        }

        public String getCustomerName()    { return customerName; }
        public List<MenuItem> getItems()   { return List.copyOf(items); }
        public LocalDateTime getOrderTime(){ return orderTime; }
        public String getShopName()        { return CoffeeShop.this.name; }
    }

    // -------------------------------------------------------------------------
    // Static nested class — no reference to outer CoffeeShop instance
    // -------------------------------------------------------------------------

    public static class CoffeeShopStats {

        private int    totalOrders;
        private int    totalItemsSold;
        private double totalRevenue;

        private CoffeeShopStats() {}

        void recordOrder(Order order) {
            totalOrders++;
            totalItemsSold += order.getItems().size();
            totalRevenue   += order.getTotal();
        }

        public int    getTotalOrders()    { return totalOrders; }
        public int    getTotalItemsSold() { return totalItemsSold; }
        public double getTotalRevenue()   { return totalRevenue; }

        public void printReport() {
            System.out.println("=== Sales Statistics ===");
            System.out.println("  Total orders     : " + totalOrders);
            System.out.println("  Total items sold : " + totalItemsSold);
            System.out.printf( "  Total revenue    : $%.2f%n", totalRevenue);
        }
    }
}
