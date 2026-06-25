package org.example.coffeeshop;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CoffeeShopTest {

    // placeOrder prints to stdout — redirect to keep test output clean
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream sink = new ByteArrayOutputStream();

    private CoffeeShop shop;
    private Coffee     espresso;
    private Tea        greenTea;
    private Pastry     croissant;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(sink));
        shop      = new CoffeeShop("Test Shop");
        espresso  = new Coffee("Espresso",  3.00, Coffee.Strength.STRONG, Size.SMALL);
        greenTea  = new Tea("Green Tea",    3.00, Tea.TeaType.GREEN, Size.MEDIUM);
        croissant = new Pastry("Croissant", 2.50, false, true);
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    // -------------------------------------------------------------------------
    // addToMenu
    // -------------------------------------------------------------------------

    @Test
    void addToMenu_singleItem_appearsInMenu() {
        shop.addToMenu(espresso);
        assertEquals(1, shop.getMenu().size());
        assertTrue(shop.getMenu().contains(espresso));
    }

    @Test
    void addToMenu_multipleItems_allPresent() {
        shop.addToMenu(espresso);
        shop.addToMenu(greenTea);
        shop.addToMenu(croissant);
        assertEquals(3, shop.getMenu().size());
    }

    @Test
    void addToMenu_preservesInsertionOrder() {
        shop.addToMenu(espresso);
        shop.addToMenu(greenTea);
        assertSame(espresso,  shop.getMenu().get(0));
        assertSame(greenTea,  shop.getMenu().get(1));
    }

    // -------------------------------------------------------------------------
    // createOrder / Order basics
    // -------------------------------------------------------------------------

    @Nested
    class OrderTest {

        @Test
        void createOrder_hasCorrectCustomerName() {
            CoffeeShop.Order order = shop.createOrder("Alice");
            assertEquals("Alice", order.getCustomerName());
        }

        @Test
        void createOrder_initiallyEmpty() {
            CoffeeShop.Order order = shop.createOrder("Alice");
            assertTrue(order.getItems().isEmpty());
        }

        @Test
        void createOrder_orderTimeNotNull() {
            LocalDateTime before = LocalDateTime.now();
            CoffeeShop.Order order = shop.createOrder("Alice");
            LocalDateTime after = LocalDateTime.now();
            assertNotNull(order.getOrderTime());
            assertFalse(order.getOrderTime().isBefore(before));
            assertFalse(order.getOrderTime().isAfter(after));
        }

        @Test
        void createOrder_shopNameMatchesEnclosingShop() {
            CoffeeShop.Order order = shop.createOrder("Alice");
            assertEquals("Test Shop", order.getShopName());
        }

        @Test
        void addItem_isFluent_returnsSameOrder() {
            CoffeeShop.Order order = shop.createOrder("Alice");
            assertSame(order, order.addItem(espresso));
        }

        @Test
        void addItem_itemAppearsInOrder() {
            CoffeeShop.Order order = shop.createOrder("Alice");
            order.addItem(espresso);
            assertTrue(order.getItems().contains(espresso));
        }

        @Test
        void addItem_multipleItems_allPresent() {
            CoffeeShop.Order order = shop.createOrder("Alice");
            order.addItem(espresso).addItem(greenTea).addItem(croissant);
            assertEquals(3, order.getItems().size());
        }

        @Test
        void getItems_returnsUnmodifiableCopy() {
            CoffeeShop.Order order = shop.createOrder("Alice");
            order.addItem(espresso);
            assertThrows(UnsupportedOperationException.class,
                    () -> order.getItems().add(greenTea));
        }

        @Test
        void getTotal_emptyOrder_isZero() {
            CoffeeShop.Order order = shop.createOrder("Alice");
            assertEquals(0.0, order.getTotal(), 0.001);
        }

        @Test
        void getTotal_singleItem_equalsItemPrice() {
            CoffeeShop.Order order = shop.createOrder("Alice");
            order.addItem(espresso);
            assertEquals(espresso.getPrice(), order.getTotal(), 0.001);
        }

        @Test
        void getTotal_multipleItems_sumOfPrices() {
            CoffeeShop.Order order = shop.createOrder("Alice");
            order.addItem(espresso).addItem(croissant);
            double expected = espresso.getPrice() + croissant.getPrice();
            assertEquals(expected, order.getTotal(), 0.001);
        }
    }

    // -------------------------------------------------------------------------
    // placeOrder
    // -------------------------------------------------------------------------

    @Test
    void placeOrder_orderAddedToList() {
        CoffeeShop.Order order = shop.createOrder("Alice");
        order.addItem(espresso);
        shop.placeOrder(order);
        assertEquals(1, shop.getOrders().size());
        assertTrue(shop.getOrders().contains(order));
    }

    @Test
    void placeOrder_multipleOrders_allRecorded() {
        shop.placeOrder(shop.createOrder("Alice").addItem(espresso));
        shop.placeOrder(shop.createOrder("Bob").addItem(greenTea));
        assertEquals(2, shop.getOrders().size());
    }

    @Test
    void placeOrder_outputContainsCustomerName() {
        shop.placeOrder(shop.createOrder("Alice").addItem(espresso));
        assertTrue(sink.toString().contains("Alice"));
    }

    @Test
    void placeOrder_preparableItemsProducePrepareOutput() {
        shop.placeOrder(shop.createOrder("Alice").addItem(espresso));
        assertTrue(sink.toString().contains("Preparing"));
    }

    @Test
    void placeOrder_nonPreparableItemProducesServingOutput() {
        shop.placeOrder(shop.createOrder("Alice").addItem(croissant));
        assertTrue(sink.toString().contains("Serving"));
    }

    // -------------------------------------------------------------------------
    // CoffeeShopStats
    // -------------------------------------------------------------------------

    @Nested
    class StatsTest {

        @Test
        void initialStats_allZero() {
            CoffeeShop.CoffeeShopStats stats = shop.getStats();
            assertEquals(0, stats.getTotalOrders());
            assertEquals(0, stats.getTotalItemsSold());
            assertEquals(0.0, stats.getTotalRevenue(), 0.001);
        }

        @Test
        void afterOneOrder_totalOrdersIsOne() {
            shop.placeOrder(shop.createOrder("Alice").addItem(espresso));
            assertEquals(1, shop.getStats().getTotalOrders());
        }

        @Test
        void afterOneOrder_totalItemsSold_matchesOrderSize() {
            shop.placeOrder(shop.createOrder("Alice")
                    .addItem(espresso)
                    .addItem(croissant));
            assertEquals(2, shop.getStats().getTotalItemsSold());
        }

        @Test
        void afterOneOrder_totalRevenue_matchesOrderTotal() {
            CoffeeShop.Order order = shop.createOrder("Alice")
                    .addItem(espresso)
                    .addItem(croissant);
            double expected = order.getTotal();
            shop.placeOrder(order);
            assertEquals(expected, shop.getStats().getTotalRevenue(), 0.001);
        }

        @Test
        void stats_accumulatesAcrossOrders() {
            shop.placeOrder(shop.createOrder("Alice").addItem(espresso));
            shop.placeOrder(shop.createOrder("Bob").addItem(greenTea).addItem(croissant));
            assertEquals(2, shop.getStats().getTotalOrders());
            assertEquals(3, shop.getStats().getTotalItemsSold());
        }

        @Test
        void stats_totalRevenue_sumOfAllOrders() {
            double r1 = espresso.getPrice();
            double r2 = greenTea.getPrice() + croissant.getPrice();
            shop.placeOrder(shop.createOrder("Alice").addItem(espresso));
            shop.placeOrder(shop.createOrder("Bob").addItem(greenTea).addItem(croissant));
            assertEquals(r1 + r2, shop.getStats().getTotalRevenue(), 0.001);
        }
    }
}
