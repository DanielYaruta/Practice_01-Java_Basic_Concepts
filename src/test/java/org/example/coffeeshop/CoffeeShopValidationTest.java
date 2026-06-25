package org.example.coffeeshop;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class CoffeeShopValidationTest {

    private final PrintStream originalOut = System.out;

    @BeforeEach
    void suppressOutput() { System.setOut(new PrintStream(new ByteArrayOutputStream())); }

    @AfterEach
    void restoreOutput() { System.setOut(originalOut); }

    // --- CoffeeShop constructor ---

    @Test
    void constructor_nullName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new CoffeeShop(null));
    }

    @Test
    void constructor_blankName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new CoffeeShop("  "));
    }

    @Test
    void constructor_validName_doesNotThrow() {
        assertDoesNotThrow(() -> new CoffeeShop("JavaBeans"));
    }

    // --- addToMenu ---

    @Test
    void addToMenu_nullItem_throwsNullPointerException() {
        CoffeeShop shop = new CoffeeShop("Shop");
        assertThrows(NullPointerException.class, () -> shop.addToMenu(null));
    }

    // --- createOrder ---

    @Test
    void createOrder_nullCustomerName_throwsNullPointerException() {
        CoffeeShop shop = new CoffeeShop("Shop");
        assertThrows(NullPointerException.class, () -> shop.createOrder(null));
    }

    @Test
    void createOrder_blankCustomerName_throwsIllegalArgumentException() {
        CoffeeShop shop = new CoffeeShop("Shop");
        assertThrows(IllegalArgumentException.class, () -> shop.createOrder("  "));
    }

    @Test
    void createOrder_validName_doesNotThrow() {
        CoffeeShop shop = new CoffeeShop("Shop");
        assertDoesNotThrow(() -> shop.createOrder("Alice"));
    }

    // --- placeOrder ---

    @Test
    void placeOrder_nullOrder_throwsNullPointerException() {
        CoffeeShop shop = new CoffeeShop("Shop");
        assertThrows(NullPointerException.class, () -> shop.placeOrder(null));
    }

    // --- Order.addItem ---

    @Test
    void addItem_nullItem_throwsNullPointerException() {
        CoffeeShop shop = new CoffeeShop("Shop");
        CoffeeShop.Order order = shop.createOrder("Alice");
        assertThrows(NullPointerException.class, () -> order.addItem(null));
    }
}
