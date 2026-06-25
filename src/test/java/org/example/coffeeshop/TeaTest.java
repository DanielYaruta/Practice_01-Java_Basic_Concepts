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

class TeaTest {

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
    // Price calculation
    // -------------------------------------------------------------------------

    @Test
    void price_small_appliesSmallMultiplier() {
        Tea tea = new Tea("Green Tea", 3.00, Tea.TeaType.GREEN, Size.SMALL);
        assertEquals(3.00 * Size.SMALL.getPriceMultiplier(), tea.getPrice(), 0.001);
    }

    @Test
    void price_medium_appliesMediumMultiplier() {
        Tea tea = new Tea("Green Tea", 3.00, Tea.TeaType.GREEN, Size.MEDIUM);
        assertEquals(3.00 * Size.MEDIUM.getPriceMultiplier(), tea.getPrice(), 0.001);
    }

    @Test
    void price_large_appliesLargeMultiplier() {
        Tea tea = new Tea("Green Tea", 3.00, Tea.TeaType.GREEN, Size.LARGE);
        assertEquals(3.00 * Size.LARGE.getPriceMultiplier(), tea.getPrice(), 0.001);
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    @Test
    void getName_returnsConstructorValue() {
        Tea tea = new Tea("Chamomile", 2.50, Tea.TeaType.HERBAL, Size.MEDIUM);
        assertEquals("Chamomile", tea.getName());
    }

    @Test
    void getType_returnsConstructorValue() {
        Tea tea = new Tea("X", 3.00, Tea.TeaType.OOLONG, Size.SMALL);
        assertEquals(Tea.TeaType.OOLONG, tea.getType());
    }

    @Test
    void getSize_returnsConstructorValue() {
        Tea tea = new Tea("X", 3.00, Tea.TeaType.BLACK, Size.LARGE);
        assertEquals(Size.LARGE, tea.getSize());
    }

    // -------------------------------------------------------------------------
    // getDescription
    // -------------------------------------------------------------------------

    @Test
    void getDescription_containsTeaTag() {
        Tea tea = new Tea("X", 3.00, Tea.TeaType.GREEN, Size.MEDIUM);
        assertTrue(tea.getDescription().contains("Tea"));
    }

    @Test
    void getDescription_containsTypeName() {
        Tea tea = new Tea("X", 3.00, Tea.TeaType.HERBAL, Size.MEDIUM);
        assertTrue(tea.getDescription().contains("Herbal"));
    }

    @Test
    void getDescription_containsSize() {
        Tea tea = new Tea("X", 3.00, Tea.TeaType.GREEN, Size.LARGE);
        assertTrue(tea.getDescription().contains("Large"));
    }

    @Test
    void getDescription_containsSteepingTime() {
        Tea tea = new Tea("X", 3.00, Tea.TeaType.GREEN, Size.MEDIUM);
        // GREEN steeps 3 minutes
        assertTrue(tea.getDescription().contains("3 min"));
    }

    // -------------------------------------------------------------------------
    // prepare() output
    // -------------------------------------------------------------------------

    @Test
    void prepare_outputContainsName() {
        Tea tea = new Tea("Green Tea", 3.00, Tea.TeaType.GREEN, Size.MEDIUM);
        tea.prepare();
        assertTrue(captured().contains("Green Tea"));
    }

    @Test
    void prepare_outputContainsHeatingWater() {
        Tea tea = new Tea("X", 3.00, Tea.TeaType.BLACK, Size.MEDIUM);
        tea.prepare();
        assertTrue(captured().contains("Heating water"));
    }

    @Test
    void prepare_outputContainsSteeping() {
        Tea tea = new Tea("X", 3.00, Tea.TeaType.GREEN, Size.MEDIUM);
        tea.prepare();
        assertTrue(captured().contains("Steeping"));
    }

    @Test
    void prepare_green_uses75Celsius() {
        Tea tea = new Tea("X", 3.00, Tea.TeaType.GREEN, Size.MEDIUM);
        tea.prepare();
        assertTrue(captured().contains("75"));
    }

    @Test
    void prepare_black_uses95Celsius() {
        Tea tea = new Tea("X", 3.00, Tea.TeaType.BLACK, Size.MEDIUM);
        tea.prepare();
        assertTrue(captured().contains("95"));
    }

    @Test
    void prepare_green_steeps3Minutes() {
        Tea tea = new Tea("X", 3.00, Tea.TeaType.GREEN, Size.MEDIUM);
        tea.prepare();
        assertTrue(captured().contains("3 minutes"));
    }

    @Test
    void prepare_herbal_steeps5Minutes() {
        Tea tea = new Tea("X", 3.00, Tea.TeaType.HERBAL, Size.MEDIUM);
        tea.prepare();
        assertTrue(captured().contains("5 minutes"));
    }

    @Test
    void prepare_outputEndsWithReady() {
        Tea tea = new Tea("Green Tea", 3.00, Tea.TeaType.GREEN, Size.SMALL);
        tea.prepare();
        assertTrue(captured().contains("is ready!"));
    }

    // -------------------------------------------------------------------------
    // TeaType enum
    // -------------------------------------------------------------------------

    @Nested
    class TeaTypeTest {

        @Test
        void hasFourValues() {
            assertEquals(4, Tea.TeaType.values().length);
        }

        @ParameterizedTest
        @EnumSource(Tea.TeaType.class)
        void toString_neverNullOrBlank(Tea.TeaType type) {
            assertFalse(type.toString().isBlank());
        }

        @ParameterizedTest
        @EnumSource(Tea.TeaType.class)
        void waterTemp_positive(Tea.TeaType type) {
            assertTrue(type.getWaterTempCelsius() > 0);
        }

        @ParameterizedTest
        @EnumSource(Tea.TeaType.class)
        void steepingMinutes_positive(Tea.TeaType type) {
            assertTrue(type.getSteepingMinutes() > 0);
        }

        @Test
        void green_waterTemp()     { assertEquals(75, Tea.TeaType.GREEN.getWaterTempCelsius()); }

        @Test
        void black_waterTemp()     { assertEquals(95, Tea.TeaType.BLACK.getWaterTempCelsius()); }

        @Test
        void herbal_waterTemp()    { assertEquals(85, Tea.TeaType.HERBAL.getWaterTempCelsius()); }

        @Test
        void oolong_waterTemp()    { assertEquals(80, Tea.TeaType.OOLONG.getWaterTempCelsius()); }

        @Test
        void green_steepingTime()  { assertEquals(3, Tea.TeaType.GREEN.getSteepingMinutes()); }

        @Test
        void black_steepingTime()  { assertEquals(4, Tea.TeaType.BLACK.getSteepingMinutes()); }

        @Test
        void herbal_steepingTime() { assertEquals(5, Tea.TeaType.HERBAL.getSteepingMinutes()); }

        @Test
        void oolong_steepingTime() { assertEquals(4, Tea.TeaType.OOLONG.getSteepingMinutes()); }
    }
}
