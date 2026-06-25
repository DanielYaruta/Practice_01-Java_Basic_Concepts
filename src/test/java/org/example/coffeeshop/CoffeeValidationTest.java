package org.example.coffeeshop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoffeeValidationTest {

    @Test
    void constructor_nullStrength_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> new Coffee("Espresso", 3.00, null, Size.SMALL));
    }

    @Test
    void constructor_nullSize_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> new Coffee("Espresso", 3.00, Coffee.Strength.STRONG, null));
    }

    @Test
    void constructor_negativeBasePrice_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Coffee("Espresso", -1.00, Coffee.Strength.STRONG, Size.SMALL));
    }

    @Test
    void constructor_nullName_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> new Coffee(null, 3.00, Coffee.Strength.STRONG, Size.SMALL));
    }

    @Test
    void constructor_blankName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Coffee("  ", 3.00, Coffee.Strength.STRONG, Size.SMALL));
    }

    @Test
    void constructor_zeroBasePrice_doesNotThrow() {
        assertDoesNotThrow(
                () -> new Coffee("Free Coffee", 0.0, Coffee.Strength.LIGHT, Size.SMALL));
    }

    @Test
    void constructor_validArguments_doesNotThrow() {
        assertDoesNotThrow(
                () -> new Coffee("Latte", 4.50, Coffee.Strength.MEDIUM, Size.LARGE));
    }
}
