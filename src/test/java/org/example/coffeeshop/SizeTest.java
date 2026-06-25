package org.example.coffeeshop;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class SizeTest {

    // --- All values present ---

    @Test
    void enumHasThreeValues() {
        assertEquals(3, Size.values().length);
    }

    @Test
    void allExpectedValuesExist() {
        assertDoesNotThrow(() -> {
            Size.valueOf("SMALL");
            Size.valueOf("MEDIUM");
            Size.valueOf("LARGE");
        });
    }

    // --- displayName ---

    @Test
    void small_displayName()  { assertEquals("Small",  Size.SMALL.getDisplayName()); }

    @Test
    void medium_displayName() { assertEquals("Medium", Size.MEDIUM.getDisplayName()); }

    @Test
    void large_displayName()  { assertEquals("Large",  Size.LARGE.getDisplayName()); }

    // --- priceMultiplier exact values ---

    @Test
    void small_priceMultiplier()  { assertEquals(0.8, Size.SMALL.getPriceMultiplier(),  0.001); }

    @Test
    void medium_priceMultiplier() { assertEquals(1.0, Size.MEDIUM.getPriceMultiplier(), 0.001); }

    @Test
    void large_priceMultiplier()  { assertEquals(1.3, Size.LARGE.getPriceMultiplier(),  0.001); }

    // --- multiplier ordering: SMALL < MEDIUM < LARGE ---

    @Test
    void multiplierOrdering_smallLessThanMedium() {
        assertTrue(Size.SMALL.getPriceMultiplier() < Size.MEDIUM.getPriceMultiplier());
    }

    @Test
    void multiplierOrdering_mediumLessThanLarge() {
        assertTrue(Size.MEDIUM.getPriceMultiplier() < Size.LARGE.getPriceMultiplier());
    }

    // --- toString == displayName (contract) ---

    @ParameterizedTest
    @EnumSource(Size.class)
    void toString_equalsDisplayName(Size size) {
        assertEquals(size.getDisplayName(), size.toString());
    }

    // --- getDisplayName contract ---

    @ParameterizedTest
    @EnumSource(Size.class)
    void getDisplayName_neverNullOrBlank(Size size) {
        assertNotNull(size.getDisplayName());
        assertFalse(size.getDisplayName().isBlank());
    }

    @ParameterizedTest
    @EnumSource(Size.class)
    void getDisplayName_doesNotEqualEnumName(Size size) {
        assertNotEquals(size.name(), size.getDisplayName());
    }

    @ParameterizedTest
    @EnumSource(Size.class)
    void priceMultiplier_positive(Size size) {
        assertTrue(size.getPriceMultiplier() > 0);
    }

    // --- valueOf / identity ---

    @Test
    void valueOf_returnsCorrectConstant() {
        assertSame(Size.LARGE, Size.valueOf("LARGE"));
    }

    @Test
    void valueOf_unknownName_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> Size.valueOf("EXTRA_LARGE"));
    }

    @ParameterizedTest
    @EnumSource(Size.class)
    void sameConstant_isEqualToItself(Size size) {
        assertSame(size, Size.valueOf(size.name()));
    }
}
