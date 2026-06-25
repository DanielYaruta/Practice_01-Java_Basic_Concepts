package org.example.taskmanager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contract tests for the Manageable interface.
 * Verifies safety guarantees that every implementation must uphold,
 * independent of state-change semantics (which are implementation-specific).
 * JUnit 5 inherits all @Test methods into concrete subclasses automatically.
 */
abstract class ManageableContractTest {

    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream sink = new ByteArrayOutputStream();

    /** Returns a fresh Manageable in its initial (unstarted) state. */
    protected abstract Manageable create();

    @BeforeEach
    void suppressOutput() {
        System.setOut(new PrintStream(sink));
    }

    @AfterEach
    void restoreOutput() {
        System.setOut(originalOut);
    }

    // --- No method throws exceptions ---

    @Test
    void assign_doesNotThrow() {
        assertDoesNotThrow(() -> create().assign("Alice"));
    }

    @Test
    void start_doesNotThrow() {
        assertDoesNotThrow(() -> create().start());
    }

    @Test
    void complete_doesNotThrow() {
        assertDoesNotThrow(() -> create().complete());
    }

    // --- Safe sequential calls ---

    @Test
    void assign_thenStart_doesNotThrow() {
        assertDoesNotThrow(() -> {
            Manageable m = create();
            m.assign("Alice");
            m.start();
        });
    }

    @Test
    void assign_thenStart_thenComplete_doesNotThrow() {
        assertDoesNotThrow(() -> {
            Manageable m = create();
            m.assign("Alice");
            m.start();
            m.complete();
        });
    }

    @Test
    void complete_calledTwice_doesNotThrow() {
        assertDoesNotThrow(() -> {
            Manageable m = create();
            m.complete();
            m.complete();
        });
    }

    @Test
    void start_calledTwice_doesNotThrow() {
        assertDoesNotThrow(() -> {
            Manageable m = create();
            m.start();
            m.start();
        });
    }

    @Test
    void complete_thenStart_doesNotThrow() {
        assertDoesNotThrow(() -> {
            Manageable m = create();
            m.complete();
            m.start();
        });
    }

    // --- assign rejects blank/empty strings ---

    @Test
    void assign_emptyString_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> create().assign(""));
    }

    @Test
    void assign_blankString_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> create().assign("   "));
    }

    @Test
    void assign_calledTwice_doesNotThrow() {
        assertDoesNotThrow(() -> {
            Manageable m = create();
            m.assign("Alice");
            m.assign("Bob");
        });
    }
}
