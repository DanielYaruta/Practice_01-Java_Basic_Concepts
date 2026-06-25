package org.example.smarthome;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contract tests for the Controllable interface.
 * Each concrete subclass supplies a SmartDevice implementation via factory methods.
 * JUnit 5 inherits all @Test methods into the subclass and runs them automatically.
 */
abstract class ControllableContractTest {

    /** Returns a Controllable that is ON and starts at its default (non-boundary) value. */
    protected abstract Controllable createActive();

    /** Returns a Controllable that is OFF. */
    protected abstract Controllable createInactive();

    // --- getStatus contract ---

    @Test
    void getStatus_neverNull() {
        assertNotNull(createActive().getStatus());
    }

    @Test
    void getStatus_neverBlank() {
        assertFalse(createActive().getStatus().isBlank());
    }

    // --- no exceptions in any state ---

    @Test
    void increaseValue_whenOn_doesNotThrow() {
        assertDoesNotThrow(() -> createActive().increaseValue());
    }

    @Test
    void decreaseValue_whenOn_doesNotThrow() {
        assertDoesNotThrow(() -> createActive().decreaseValue());
    }

    @Test
    void increaseValue_whenOff_doesNotThrow() {
        assertDoesNotThrow(() -> createInactive().increaseValue());
    }

    @Test
    void decreaseValue_whenOff_doesNotThrow() {
        assertDoesNotThrow(() -> createInactive().decreaseValue());
    }

    // --- ON state: value must change ---

    @Test
    void increaseValue_whenOn_changesStatus() {
        Controllable c = createActive();
        String before = c.getStatus();
        c.increaseValue();
        assertNotEquals(before, c.getStatus());
    }

    @Test
    void decreaseValue_whenOn_changesStatus() {
        Controllable c = createActive();
        String before = c.getStatus();
        c.decreaseValue();
        assertNotEquals(before, c.getStatus());
    }

    // --- OFF state: value must NOT change ---

    @Test
    void increaseValue_whenOff_doesNotChangeStatus() {
        Controllable c = createInactive();
        String before = c.getStatus();
        c.increaseValue();
        assertEquals(before, c.getStatus());
    }

    @Test
    void decreaseValue_whenOff_doesNotChangeStatus() {
        Controllable c = createInactive();
        String before = c.getStatus();
        c.decreaseValue();
        assertEquals(before, c.getStatus());
    }

    // --- symmetry: increase then decrease returns to original ---

    @Test
    void increaseValue_thenDecreaseValue_restoresStatus() {
        Controllable c = createActive();
        String before = c.getStatus();
        c.increaseValue();
        c.decreaseValue();
        assertEquals(before, c.getStatus());
    }

    @Test
    void decreaseValue_thenIncreaseValue_restoresStatus() {
        Controllable c = createActive();
        String before = c.getStatus();
        c.decreaseValue();
        c.increaseValue();
        assertEquals(before, c.getStatus());
    }
}
