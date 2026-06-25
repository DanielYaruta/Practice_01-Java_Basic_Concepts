package org.example.coffeeshop;

public class CoffeeShopDemo {

    public static void run() {
        CoffeeShop shop = new CoffeeShop("JavaBeans");

        shop.addToMenu(new Coffee("Espresso",        3.00, Coffee.Strength.STRONG, Size.SMALL));
        shop.addToMenu(new Coffee("Latte",            4.50, Coffee.Strength.MEDIUM, Size.MEDIUM));
        shop.addToMenu(new Coffee("Cappuccino",       4.00, Coffee.Strength.MEDIUM, Size.LARGE));
        shop.addToMenu(new Coffee("Americano",        3.50, Coffee.Strength.LIGHT,  Size.LARGE));
        shop.addToMenu(new Tea("Green Tea",           3.00, Tea.TeaType.GREEN,  Size.MEDIUM));
        shop.addToMenu(new Tea("English Breakfast",   3.00, Tea.TeaType.BLACK, Size.LARGE));
        shop.addToMenu(new Pastry("Croissant",        2.50, false, true));
        shop.addToMenu(new Pastry("Almond Muffin",   3.00, true, false));

        shop.printMenu();
        System.out.println();

        CoffeeShop.Order order1 = shop.createOrder("Alice")
                .addItem(new Coffee("Latte",     4.50, Coffee.Strength.MEDIUM, Size.LARGE))
                .addItem(new Pastry("Croissant", 2.50, false, true));
        shop.placeOrder(order1);

        System.out.println();

        CoffeeShop.Order order2 = shop.createOrder("Bob")
                .addItem(new Coffee("Espresso",      3.00, Coffee.Strength.STRONG, Size.SMALL))
                .addItem(new Tea("Green Tea",         3.00, Tea.TeaType.GREEN, Size.SMALL))
                .addItem(new Pastry("Almond Muffin", 3.00, true, false));
        shop.placeOrder(order2);

        System.out.println();

        CoffeeShop.Order order3 = shop.createOrder("Carol")
                .addItem(new Tea("English Breakfast", 3.00, Tea.TeaType.BLACK, Size.LARGE));
        shop.placeOrder(order3);

        System.out.println();
        shop.getStats().printReport();
    }
}
