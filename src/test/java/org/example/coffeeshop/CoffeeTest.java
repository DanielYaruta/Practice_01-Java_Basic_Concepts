package org.example.coffeeshop;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class CoffeeTest {

    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    @BeforeEach
    void redirectOutput() {
        System.setOut(new PrintStream(output));
    }

    @AfterEach
    void restoreOutput() {
        System.setOut(originalOut);
    }

    private String captured() {
        return output.toString();
    }

    // -------------------------------------------------------------------------
    // Price calculation: basePrice * size.priceMultiplier
    // -------------------------------------------------------------------------

    @Test
    void price_small_appliesSmallMultiplier() {
        Coffee coffee = new Coffee("Espresso", 3.00, Coffee.Strength.STRONG, Size.SMALL);
        assertEquals(3.00 * Size.SMALL.getPriceMultiplier(), coffee.getPrice(), 0.001);
    }

    @Test
    void price_medium_appliesMediumMultiplier() {
        Coffee coffee = new Coffee("Latte", 4.00, Coffee.Strength.MEDIUM, Size.MEDIUM);
        assertEquals(4.00 * Size.MEDIUM.getPriceMultiplier(), coffee.getPrice(), 0.001);
    }

    @Test
    void price_large_appliesLargeMultiplier() {
        Coffee coffee = new Coffee("Cappuccino", 4.00, Coffee.Strength.MEDIUM, Size.LARGE);
        assertEquals(4.00 * Size.LARGE.getPriceMultiplier(), coffee.getPrice(), 0.001);
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    @Test
    void getName_returnsConstructorValue() {
        Coffee coffee = new Coffee("Americano", 3.50, Coffee.Strength.LIGHT, Size.MEDIUM);
        assertEquals("Americano", coffee.getName());
    }

    @Test
    void getStrength_returnsConstructorValue() {
        Coffee coffee = new Coffee("X", 3.00, Coffee.Strength.STRONG, Size.SMALL);
        assertEquals(Coffee.Strength.STRONG, coffee.getStrength());
    }

    @Test
    void getSize_returnsConstructorValue() {
        Coffee coffee = new Coffee("X", 3.00, Coffee.Strength.MEDIUM, Size.LARGE);
        assertEquals(Size.LARGE, coffee.getSize());
    }

    // -------------------------------------------------------------------------
    // getDescription
    // -------------------------------------------------------------------------

    @Test
    void getDescription_containsPrice() {
        Coffee coffee = new Coffee("Latte", 4.00, Coffee.Strength.MEDIUM, Size.MEDIUM);
        assertTrue(coffee.getDescription().contains("4"));
    }

    @Test
    void getDescription_containsCoffeeTag() {
        Coffee coffee = new Coffee("X", 3.00, Coffee.Strength.LIGHT, Size.SMALL);
        assertTrue(coffee.getDescription().contains("Coffee"));
    }

    @Test
    void getDescription_containsStrength() {
        Coffee coffee = new Coffee("X", 3.00, Coffee.Strength.STRONG, Size.SMALL);
        assertTrue(coffee.getDescription().contains("Strong"));
    }

    @Test
    void getDescription_containsSize() {
        Coffee coffee = new Coffee("X", 3.00, Coffee.Strength.MEDIUM, Size.LARGE);
        assertTrue(coffee.getDescription().contains("Large"));
    }

    // -------------------------------------------------------------------------
    // prepare() output
    // -------------------------------------------------------------------------

    @Test
    void prepare_outputContainsName() {
        Coffee coffee = new Coffee("Espresso", 3.00, Coffee.Strength.STRONG, Size.SMALL);
        coffee.prepare();
        assertTrue(captured().contains("Espresso"));
    }

    @Test
    void prepare_outputContainsGrinding() {
        Coffee coffee = new Coffee("Espresso", 3.00, Coffee.Strength.STRONG, Size.SMALL);
        coffee.prepare();
        assertTrue(captured().contains("Grinding"));
    }

    @Test
    void prepare_outputContainsBrewing() {
        Coffee coffee = new Coffee("Espresso", 3.00, Coffee.Strength.STRONG, Size.SMALL);
        coffee.prepare();
        assertTrue(captured().contains("Brewing"));
    }

    @Test
    void prepare_small_noMilkStep() {
        Coffee coffee = new Coffee("Espresso", 3.00, Coffee.Strength.STRONG, Size.SMALL);
        coffee.prepare();
        assertFalse(captured().contains("milk"));
    }

    @Test
    void prepare_medium_hasMilkStep() {
        Coffee coffee = new Coffee("Latte", 4.00, Coffee.Strength.MEDIUM, Size.MEDIUM);
        coffee.prepare();
        assertTrue(captured().contains("milk"));
    }

    @Test
    void prepare_large_hasMilkStep() {
        Coffee coffee = new Coffee("Cappuccino", 4.00, Coffee.Strength.MEDIUM, Size.LARGE);
        coffee.prepare();
        assertTrue(captured().contains("milk"));
    }

    @Test
    void prepare_outputEndsWithReady() {
        Coffee coffee = new Coffee("Espresso", 3.00, Coffee.Strength.STRONG, Size.SMALL);
        coffee.prepare();
        assertTrue(captured().contains("is ready!"));
    }

    // -------------------------------------------------------------------------
    // Strength enum
    // -------------------------------------------------------------------------

    @Nested
    class StrengthTest {

        @Test
        void hasThreeValues() {
            assertEquals(3, Coffee.Strength.values().length);
        }

        @ParameterizedTest
        @EnumSource(Coffee.Strength.class)
        void toString_neverNullOrBlank(Coffee.Strength strength) {
            assertFalse(strength.toString().isBlank());
        }

        @ParameterizedTest
        @EnumSource(Coffee.Strength.class)
        void toString_doesNotEqualEnumName(Coffee.Strength strength) {
            // Strength enum uses title-case display names, not UPPER_CASE
            assertNotEquals(strength.name(), strength.toString());
        }

        @Test
        void light_toString() { assertEquals("Light",  Coffee.Strength.LIGHT.toString()); }

        @Test
        void medium_toString() { assertEquals("Medium", Coffee.Strength.MEDIUM.toString()); }

        @Test
        void strong_toString() { assertEquals("Strong", Coffee.Strength.STRONG.toString()); }
    }
}
