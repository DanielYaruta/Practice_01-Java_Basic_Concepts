package org.example.coffeeshop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PastryTest {

    // -------------------------------------------------------------------------
    // Price stored as-is (no size multiplier)
    // -------------------------------------------------------------------------

    @Test
    void price_storedAsGiven() {
        Pastry pastry = new Pastry("Croissant", 2.50, false, true);
        assertEquals(2.50, pastry.getPrice(), 0.001);
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    @Test
    void getName_returnsConstructorValue() {
        Pastry pastry = new Pastry("Muffin", 3.00, true, false);
        assertEquals("Muffin", pastry.getName());
    }

    @Test
    void isGlutenFree_true_whenConstructedWithTrue() {
        Pastry pastry = new Pastry("Muffin", 3.00, true, false);
        assertTrue(pastry.isGlutenFree());
    }

    @Test
    void isGlutenFree_false_whenConstructedWithFalse() {
        Pastry pastry = new Pastry("Croissant", 2.50, false, true);
        assertFalse(pastry.isGlutenFree());
    }

    @Test
    void isServedWarm_true_whenConstructedWithTrue() {
        Pastry pastry = new Pastry("Croissant", 2.50, false, true);
        assertTrue(pastry.isServedWarm());
    }

    @Test
    void isServedWarm_false_whenConstructedWithFalse() {
        Pastry pastry = new Pastry("Muffin", 3.00, true, false);
        assertFalse(pastry.isServedWarm());
    }

    // -------------------------------------------------------------------------
    // getDescription — gluten-free / regular
    // -------------------------------------------------------------------------

    @Test
    void getDescription_glutenFree_containsGlutenFreeTag() {
        Pastry pastry = new Pastry("Muffin", 3.00, true, false);
        assertTrue(pastry.getDescription().contains("gluten-free"));
    }

    @Test
    void getDescription_notGlutenFree_containsRegularTag() {
        Pastry pastry = new Pastry("Croissant", 2.50, false, false);
        assertTrue(pastry.getDescription().contains("regular"));
    }

    @Test
    void getDescription_glutenFree_doesNotContainRegularTag() {
        Pastry pastry = new Pastry("Muffin", 3.00, true, false);
        assertFalse(pastry.getDescription().contains("regular"));
    }

    // -------------------------------------------------------------------------
    // getDescription — warm / not warm
    // -------------------------------------------------------------------------

    @Test
    void getDescription_servedWarm_containsWarmTag() {
        Pastry pastry = new Pastry("Croissant", 2.50, false, true);
        assertTrue(pastry.getDescription().contains("warm"));
    }

    @Test
    void getDescription_notServedWarm_noWarmTag() {
        Pastry pastry = new Pastry("Muffin", 3.00, true, false);
        assertFalse(pastry.getDescription().contains("warm"));
    }

    // -------------------------------------------------------------------------
    // getDescription — always contains Pastry tag and price
    // -------------------------------------------------------------------------

    @Test
    void getDescription_containsPastryTag() {
        Pastry pastry = new Pastry("Scone", 2.00, false, true);
        assertTrue(pastry.getDescription().contains("Pastry"));
    }

    @Test
    void getDescription_containsPrice() {
        Pastry pastry = new Pastry("Scone", 2.00, false, false);
        assertTrue(pastry.getDescription().contains("2"));
    }

    // -------------------------------------------------------------------------
    // Pastry does NOT implement Preparable
    // -------------------------------------------------------------------------

    @Test
    void pastry_doesNotImplementPreparable() {
        Pastry pastry = new Pastry("Croissant", 2.50, false, true);
        assertFalse(pastry instanceof Preparable);
    }

    // -------------------------------------------------------------------------
    // All four flag combinations
    // -------------------------------------------------------------------------

    @Test
    void glutenFree_and_warm() {
        Pastry pastry = new Pastry("X", 1.00, true, true);
        String desc = pastry.getDescription();
        assertTrue(desc.contains("gluten-free"));
        assertTrue(desc.contains("warm"));
    }

    @Test
    void regular_and_warm() {
        Pastry pastry = new Pastry("X", 1.00, false, true);
        String desc = pastry.getDescription();
        assertTrue(desc.contains("regular"));
        assertTrue(desc.contains("warm"));
    }

    @Test
    void glutenFree_and_notWarm() {
        Pastry pastry = new Pastry("X", 1.00, true, false);
        String desc = pastry.getDescription();
        assertTrue(desc.contains("gluten-free"));
        assertFalse(desc.contains("warm"));
    }

    @Test
    void regular_and_notWarm() {
        Pastry pastry = new Pastry("X", 1.00, false, false);
        String desc = pastry.getDescription();
        assertTrue(desc.contains("regular"));
        assertFalse(desc.contains("warm"));
    }
}
