package org.example.taskmanager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class TaskStatusTest {

    // --- All values present ---

    @Test
    void enumHasThreeValues() {
        assertEquals(3, TaskStatus.values().length);
    }

    @Test
    void allExpectedValuesExist() {
        assertDoesNotThrow(() -> {
            TaskStatus.valueOf("TODO");
            TaskStatus.valueOf("IN_PROGRESS");
            TaskStatus.valueOf("DONE");
        });
    }

    // --- displayName exact values ---

    @Test
    void todo_displayName()       { assertEquals("To Do",       TaskStatus.TODO.getDisplayName()); }

    @Test
    void inProgress_displayName() { assertEquals("In Progress", TaskStatus.IN_PROGRESS.getDisplayName()); }

    @Test
    void done_displayName()       { assertEquals("Done",        TaskStatus.DONE.getDisplayName()); }

    // --- toString == displayName ---

    @ParameterizedTest
    @EnumSource(TaskStatus.class)
    void toString_equalsDisplayName(TaskStatus status) {
        assertEquals(status.getDisplayName(), status.toString());
    }

    // --- getDisplayName contract ---

    @ParameterizedTest
    @EnumSource(TaskStatus.class)
    void getDisplayName_neverNullOrBlank(TaskStatus status) {
        assertNotNull(status.getDisplayName());
        assertFalse(status.getDisplayName().isBlank());
    }

    @ParameterizedTest
    @EnumSource(TaskStatus.class)
    void getDisplayName_doesNotEqualEnumName(TaskStatus status) {
        assertNotEquals(status.name(), status.getDisplayName());
    }

    // --- Ordinal ordering: TODO < IN_PROGRESS < DONE ---
    // TaskComparator.STATUS sorts by compareTo(), which uses ordinal.
    // These tests pin the order so a reordering of the enum is caught immediately.

    @Test
    void todo_isLessThan_inProgress() {
        assertTrue(TaskStatus.TODO.compareTo(TaskStatus.IN_PROGRESS) < 0);
    }

    @Test
    void inProgress_isLessThan_done() {
        assertTrue(TaskStatus.IN_PROGRESS.compareTo(TaskStatus.DONE) < 0);
    }

    @Test
    void todo_isLessThan_done() {
        assertTrue(TaskStatus.TODO.compareTo(TaskStatus.DONE) < 0);
    }

    // --- valueOf / identity ---

    @Test
    void valueOf_returnsCorrectConstant() {
        assertSame(TaskStatus.IN_PROGRESS, TaskStatus.valueOf("IN_PROGRESS"));
    }

    @Test
    void valueOf_unknownName_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> TaskStatus.valueOf("CANCELLED"));
    }

    @ParameterizedTest
    @EnumSource(TaskStatus.class)
    void sameConstant_isEqualToItself(TaskStatus status) {
        assertSame(status, TaskStatus.valueOf(status.name()));
    }
}
