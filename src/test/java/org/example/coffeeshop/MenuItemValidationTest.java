package org.example.coffeeshop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MenuItemValidationTest {

    // MenuItem is abstract — tested through Pastry (no extra validation of its own)

    @Test
    void constructor_nullName_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> new Pastry(null, 2.50, false, false));
    }

    @Test
    void constructor_blankName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Pastry("   ", 2.50, false, false));
    }

    @Test
    void constructor_emptyName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Pastry("", 2.50, false, false));
    }

    @Test
    void constructor_negativePrice_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Pastry("Croissant", -1.00, false, false));
    }

    @Test
    void constructor_zeroPrice_doesNotThrow() {
        assertDoesNotThrow(() -> new Pastry("Free Sample", 0.0, false, false));
    }

    @Test
    void constructor_validArguments_doesNotThrow() {
        assertDoesNotThrow(() -> new Pastry("Croissant", 2.50, true, true));
    }
}
