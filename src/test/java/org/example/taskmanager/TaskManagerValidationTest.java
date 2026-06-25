package org.example.taskmanager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerValidationTest {

    private final PrintStream originalOut = System.out;

    @BeforeEach
    void suppressOutput() { System.setOut(new PrintStream(new ByteArrayOutputStream())); }

    @AfterEach
    void restoreOutput() { System.setOut(originalOut); }

    // --- addProject ---

    @Test
    void addProject_null_throwsNullPointerException() {
        TaskManager manager = new TaskManager();
        assertThrows(NullPointerException.class, () -> manager.addProject(null));
    }

    @Test
    void addProject_valid_doesNotThrow() {
        TaskManager manager = new TaskManager();
        assertDoesNotThrow(() -> manager.addProject(new Project("P", "")));
    }

    // --- removeProject ---

    @Test
    void removeProject_null_throwsNullPointerException() {
        TaskManager manager = new TaskManager();
        assertThrows(NullPointerException.class, () -> manager.removeProject(null));
    }

    // --- findByName ---

    @Test
    void findByName_null_throwsNullPointerException() {
        TaskManager manager = new TaskManager();
        assertThrows(NullPointerException.class, () -> manager.findByName(null));
    }

    @Test
    void findByName_blank_throwsIllegalArgumentException() {
        TaskManager manager = new TaskManager();
        assertThrows(IllegalArgumentException.class, () -> manager.findByName("  "));
    }

    @Test
    void findByName_empty_throwsIllegalArgumentException() {
        TaskManager manager = new TaskManager();
        assertThrows(IllegalArgumentException.class, () -> manager.findByName(""));
    }

    @Test
    void findByName_valid_doesNotThrow() {
        TaskManager manager = new TaskManager();
        assertDoesNotThrow(() -> manager.findByName("Alpha"));
    }
}
