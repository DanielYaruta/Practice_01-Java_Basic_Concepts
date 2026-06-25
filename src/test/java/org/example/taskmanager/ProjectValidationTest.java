package org.example.taskmanager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ProjectValidationTest {

    private final PrintStream originalOut = System.out;

    @BeforeEach
    void suppressOutput() { System.setOut(new PrintStream(new ByteArrayOutputStream())); }

    @AfterEach
    void restoreOutput() { System.setOut(originalOut); }

    // --- constructor ---

    @Test
    void constructor_nullName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Project(null, "desc"));
    }

    @Test
    void constructor_blankName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Project("  ", "desc"));
    }

    @Test
    void constructor_nullDescription_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Project("Alpha", null));
    }

    @Test
    void constructor_emptyDescription_doesNotThrow() {
        assertDoesNotThrow(() -> new Project("Alpha", ""));
    }

    @Test
    void constructor_validArguments_doesNotThrow() {
        assertDoesNotThrow(() -> new Project("Alpha", "First project"));
    }

    // --- addTask ---

    @Test
    void addTask_null_throwsNullPointerException() {
        Project p = new Project("P", "");
        assertThrows(NullPointerException.class, () -> p.addTask(null));
    }

    // --- assign ---

    @Test
    void assign_null_throwsNullPointerException() {
        Project p = new Project("P", "");
        assertThrows(NullPointerException.class, () -> p.assign(null));
    }

    @Test
    void assign_blank_throwsIllegalArgumentException() {
        Project p = new Project("P", "");
        assertThrows(IllegalArgumentException.class, () -> p.assign("  "));
    }

    @Test
    void assign_valid_doesNotThrow() {
        Project p = new Project("P", "");
        assertDoesNotThrow(() -> p.assign("Alice"));
    }
}
