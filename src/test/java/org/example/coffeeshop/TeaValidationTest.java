package org.example.coffeeshop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeaValidationTest {

    @Test
    void constructor_nullType_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> new Tea("Green Tea", 3.00, null, Size.MEDIUM));
    }

    @Test
    void constructor_nullSize_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> new Tea("Green Tea", 3.00, Tea.TeaType.GREEN, null));
    }

    @Test
    void constructor_negativeBasePrice_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Tea("Green Tea", -0.50, Tea.TeaType.GREEN, Size.SMALL));
    }

    @Test
    void constructor_nullName_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> new Tea(null, 3.00, Tea.TeaType.BLACK, Size.MEDIUM));
    }

    @Test
    void constructor_blankName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Tea("", 3.00, Tea.TeaType.BLACK, Size.MEDIUM));
    }

    @Test
    void constructor_zeroBasePrice_doesNotThrow() {
        assertDoesNotThrow(
                () -> new Tea("Free Tea", 0.0, Tea.TeaType.HERBAL, Size.SMALL));
    }

    @Test
    void constructor_validArguments_doesNotThrow() {
        assertDoesNotThrow(
                () -> new Tea("Green Tea", 3.00, Tea.TeaType.GREEN, Size.LARGE));
    }
}
