package org.example.taskmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    // Fixed dates independent of LocalDate.now() so tests never break over time
    private static final LocalDate PAST   = LocalDate.of(2000, 1, 1);
    private static final LocalDate FUTURE = LocalDate.of(2099, 12, 31);

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task("Write tests", "Cover all methods", FUTURE);
    }

    // -------------------------------------------------------------------------
    // Initial state
    // -------------------------------------------------------------------------

    @Test
    void initialStatus_isTodo() {
        assertEquals(TaskStatus.TODO, task.getStatus());
    }

    @Test
    void initialAssignee_isNull() {
        assertNull(task.getAssignee());
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @Test
    void getTitle_returnsConstructorValue() {
        assertEquals("Write tests", task.getTitle());
    }

    @Test
    void getDescription_returnsConstructorValue() {
        assertEquals("Cover all methods", task.getDescription());
    }

    @Test
    void getDueDate_returnsConstructorValue() {
        assertEquals(FUTURE, task.getDueDate());
    }

    @Test
    void setTitle_updatesTitle() {
        task.setTitle("New title");
        assertEquals("New title", task.getTitle());
    }

    @Test
    void setDescription_updatesDescription() {
        task.setDescription("New desc");
        assertEquals("New desc", task.getDescription());
    }

    @Test
    void setDueDate_updatesDate() {
        task.setDueDate(PAST);
        assertEquals(PAST, task.getDueDate());
    }

    @Test
    void setStatus_updatesStatus() {
        task.setStatus(TaskStatus.IN_PROGRESS);
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
    }

    @Test
    void setAssignee_updatesAssignee() {
        task.setAssignee("Alice");
        assertEquals("Alice", task.getAssignee());
    }

    // -------------------------------------------------------------------------
    // isOverdue()
    // -------------------------------------------------------------------------

    @Nested
    class IsOverdueTest {

        @Test
        void pastDate_todoStatus_isOverdue() {
            Task t = new Task("X", "", PAST);
            assertTrue(t.isOverdue());
        }

        @Test
        void pastDate_inProgressStatus_isOverdue() {
            Task t = new Task("X", "", PAST);
            t.setStatus(TaskStatus.IN_PROGRESS);
            assertTrue(t.isOverdue());
        }

        @Test
        void pastDate_doneStatus_notOverdue() {
            Task t = new Task("X", "", PAST);
            t.setStatus(TaskStatus.DONE);
            assertFalse(t.isOverdue());
        }

        @Test
        void futureDate_todoStatus_notOverdue() {
            Task t = new Task("X", "", FUTURE);
            assertFalse(t.isOverdue());
        }

        @Test
        void nullDate_notOverdue() {
            Task t = new Task("X", "", null);
            assertFalse(t.isOverdue());
        }
    }

    // -------------------------------------------------------------------------
    // isCompleted()
    // -------------------------------------------------------------------------

    @Nested
    class IsCompletedTest {

        @Test
        void todoStatus_notCompleted() {
            assertFalse(task.isCompleted());
        }

        @Test
        void inProgressStatus_notCompleted() {
            task.setStatus(TaskStatus.IN_PROGRESS);
            assertFalse(task.isCompleted());
        }

        @Test
        void doneStatus_isCompleted() {
            task.setStatus(TaskStatus.DONE);
            assertTrue(task.isCompleted());
        }
    }

    // -------------------------------------------------------------------------
    // isAssigned()
    // -------------------------------------------------------------------------

    @Nested
    class IsAssignedTest {

        @Test
        void nullAssignee_notAssigned() {
            assertFalse(task.isAssigned());
        }

        @Test
        void blankAssignee_notAssigned() {
            task.setAssignee("   ");
            assertFalse(task.isAssigned());
        }

        @Test
        void emptyAssignee_notAssigned() {
            task.setAssignee("");
            assertFalse(task.isAssigned());
        }

        @Test
        void validAssignee_isAssigned() {
            task.setAssignee("Alice");
            assertTrue(task.isAssigned());
        }
    }

    // -------------------------------------------------------------------------
    // toString()
    // -------------------------------------------------------------------------

    @Nested
    class ToStringTest {

        @Test
        void containsTitle() {
            assertTrue(task.toString().contains("Write tests"));
        }

        @Test
        void containsStatusDisplayName() {
            assertTrue(task.toString().contains("To Do"));
        }

        @Test
        void containsDueDate() {
            assertTrue(task.toString().contains(FUTURE.toString()));
        }

        @Test
        void unassigned_containsUnassignedLabel() {
            assertTrue(task.toString().contains("unassigned"));
        }

        @Test
        void assigned_containsAssigneeName() {
            task.setAssignee("Bob");
            assertTrue(task.toString().contains("Bob"));
        }

        @Test
        void overdueTask_containsOverdueFlag() {
            Task t = new Task("Late", "", PAST);
            assertTrue(t.toString().contains("[OVERDUE]"));
        }

        @Test
        void nonOverdueTask_noOverdueFlag() {
            assertFalse(task.toString().contains("[OVERDUE]"));
        }

        @Test
        void nullDueDate_containsNoDateLabel() {
            Task t = new Task("X", "", null);
            assertTrue(t.toString().contains("no date"));
        }
    }
}
