package org.example.taskmanager;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TaskValidationTest {

    // --- constructor ---

    @Test
    void constructor_nullTitle_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> new Task(null, "desc", null));
    }

    @Test
    void constructor_blankTitle_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Task("  ", "desc", null));
    }

    @Test
    void constructor_emptyTitle_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Task("", "desc", null));
    }

    @Test
    void constructor_nullDescription_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> new Task("Title", null, null));
    }

    @Test
    void constructor_nullDueDate_doesNotThrow() {
        assertDoesNotThrow(() -> new Task("Title", "desc", null));
    }

    @Test
    void constructor_emptyDescription_doesNotThrow() {
        assertDoesNotThrow(() -> new Task("Title", "", null));
    }

    @Test
    void constructor_validArguments_doesNotThrow() {
        assertDoesNotThrow(() -> new Task("Fix bug", "desc", LocalDate.now()));
    }

    // --- setTitle ---

    @Test
    void setTitle_null_throwsNullPointerException() {
        Task task = new Task("Title", "desc", null);
        assertThrows(NullPointerException.class, () -> task.setTitle(null));
    }

    @Test
    void setTitle_blank_throwsIllegalArgumentException() {
        Task task = new Task("Title", "desc", null);
        assertThrows(IllegalArgumentException.class, () -> task.setTitle("   "));
    }

    @Test
    void setTitle_valid_doesNotThrow() {
        Task task = new Task("Title", "desc", null);
        assertDoesNotThrow(() -> task.setTitle("New Title"));
    }

    // --- setDescription ---

    @Test
    void setDescription_null_throwsNullPointerException() {
        Task task = new Task("Title", "desc", null);
        assertThrows(NullPointerException.class, () -> task.setDescription(null));
    }

    @Test
    void setDescription_empty_doesNotThrow() {
        Task task = new Task("Title", "desc", null);
        assertDoesNotThrow(() -> task.setDescription(""));
    }

    // --- setStatus ---

    @Test
    void setStatus_null_throwsNullPointerException() {
        Task task = new Task("Title", "desc", null);
        assertThrows(NullPointerException.class, () -> task.setStatus(null));
    }

    @Test
    void setStatus_valid_doesNotThrow() {
        Task task = new Task("Title", "desc", null);
        assertDoesNotThrow(() -> task.setStatus(TaskStatus.DONE));
    }

    // --- setAssignee: null is allowed (means unassign) ---

    @Test
    void setAssignee_null_doesNotThrow() {
        Task task = new Task("Title", "desc", null);
        assertDoesNotThrow(() -> task.setAssignee(null));
    }

    @Test
    void setAssignee_blank_doesNotThrow() {
        Task task = new Task("Title", "desc", null);
        assertDoesNotThrow(() -> task.setAssignee("  "));
    }
}
