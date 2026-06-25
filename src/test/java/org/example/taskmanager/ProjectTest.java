package org.example.taskmanager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProjectTest {

    private static final LocalDate D1 = LocalDate.of(2099, 1, 1);
    private static final LocalDate D2 = LocalDate.of(2099, 6, 1);
    private static final LocalDate D3 = LocalDate.of(2099, 12, 1);

    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream sink = new ByteArrayOutputStream();

    private Project project;
    private Task    taskA;   // title "Alpha", D2, TODO
    private Task    taskB;   // title "Beta",  D1, IN_PROGRESS
    private Task    taskC;   // title "Gamma", D3, DONE
    private Task    taskNull;// title "Delta", no date, TODO

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(sink));
        project  = new Project("TestProject", "A test project");
        taskA    = new Task("Alpha", "desc", D2);
        taskB    = new Task("Beta",  "desc", D1);
        taskB.setStatus(TaskStatus.IN_PROGRESS);
        taskC    = new Task("Gamma", "desc", D3);
        taskC.setStatus(TaskStatus.DONE);
        taskNull = new Task("Delta", "desc", null);
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    // -------------------------------------------------------------------------
    // Initial state
    // -------------------------------------------------------------------------

    @Test
    void initialStatus_isTodo() {
        assertEquals(TaskStatus.TODO, project.getStatus());
    }

    @Test
    void initialManager_isNull() {
        assertNull(project.getManager());
    }

    @Test
    void initialTasks_isEmpty() {
        assertTrue(project.getTasks().isEmpty());
    }

    @Test
    void getName_returnsConstructorValue() {
        assertEquals("TestProject", project.getName());
    }

    @Test
    void getDescription_returnsConstructorValue() {
        assertEquals("A test project", project.getDescription());
    }

    // -------------------------------------------------------------------------
    // Manageable: assign / start / complete
    // -------------------------------------------------------------------------

    @Nested
    class ManageableTest {

        @Test
        void assign_setsManager() {
            project.assign("Alice");
            assertEquals("Alice", project.getManager());
        }

        @Test
        void start_fromTodo_setsInProgress() {
            project.start();
            assertEquals(TaskStatus.IN_PROGRESS, project.getStatus());
        }

        @Test
        void start_alreadyInProgress_remainsInProgress() {
            project.start();
            project.start();
            assertEquals(TaskStatus.IN_PROGRESS, project.getStatus());
        }

        @Test
        void start_alreadyDone_remainsDone() {
            project.addTask(taskA);
            project.complete();
            project.start();
            assertEquals(TaskStatus.DONE, project.getStatus());
        }

        @Test
        void complete_setsProjectDone() {
            project.complete();
            assertEquals(TaskStatus.DONE, project.getStatus());
        }

        @Test
        void complete_setsAllTasksToDone() {
            project.addTask(taskA);
            project.addTask(taskB);
            project.complete();
            project.getTasks().forEach(t ->
                    assertEquals(TaskStatus.DONE, t.getStatus()));
        }

        @Test
        void complete_withNoTasks_stillSetsDone() {
            project.complete();
            assertEquals(TaskStatus.DONE, project.getStatus());
        }
    }

    // -------------------------------------------------------------------------
    // Task management: add / remove / getTasks / countByStatus
    // -------------------------------------------------------------------------

    @Nested
    class TaskManagementTest {

        @Test
        void addTask_taskAppearsInList() {
            project.addTask(taskA);
            assertTrue(project.getTasks().contains(taskA));
        }

        @Test
        void addTask_multipleItems_allPresent() {
            project.addTask(taskA);
            project.addTask(taskB);
            project.addTask(taskC);
            assertEquals(3, project.getTasks().size());
        }

        @Test
        void removeTask_existingTask_returnsTrue() {
            project.addTask(taskA);
            assertTrue(project.removeTask(taskA));
        }

        @Test
        void removeTask_existingTask_removedFromList() {
            project.addTask(taskA);
            project.removeTask(taskA);
            assertFalse(project.getTasks().contains(taskA));
        }

        @Test
        void removeTask_notPresent_returnsFalse() {
            assertFalse(project.removeTask(taskA));
        }

        @Test
        void getTasks_returnsUnmodifiableList() {
            project.addTask(taskA);
            assertThrows(UnsupportedOperationException.class,
                    () -> project.getTasks().add(taskB));
        }

        @Test
        void countByStatus_todo() {
            project.addTask(taskA);    // TODO
            project.addTask(taskNull); // TODO
            project.addTask(taskB);    // IN_PROGRESS
            project.addTask(taskC);    // DONE
            assertEquals(2, project.countByStatus(TaskStatus.TODO));
        }

        @Test
        void countByStatus_inProgress() {
            project.addTask(taskA);
            project.addTask(taskB);
            assertEquals(1, project.countByStatus(TaskStatus.IN_PROGRESS));
        }

        @Test
        void countByStatus_done() {
            project.addTask(taskB);
            project.addTask(taskC);
            assertEquals(1, project.countByStatus(TaskStatus.DONE));
        }

        @Test
        void countByStatus_noMatchingTasks_returnsZero() {
            project.addTask(taskA);
            assertEquals(0, project.countByStatus(TaskStatus.DONE));
        }
    }

    // -------------------------------------------------------------------------
    // TaskComparator (non-static inner class)
    // -------------------------------------------------------------------------

    @Nested
    class TaskComparatorTest {

        @BeforeEach
        void addTasks() {
            project.addTask(taskA);    // "Alpha", D2, TODO
            project.addTask(taskB);    // "Beta",  D1, IN_PROGRESS
            project.addTask(taskC);    // "Gamma", D3, DONE
            project.addTask(taskNull); // "Delta", null, TODO
        }

        @Test
        void sortByTitle_alphabeticalOrder() {
            List<Task> sorted = project.getSortedTasks(Project.TaskComparator.Criterion.TITLE);
            assertEquals("Alpha", sorted.get(0).getTitle());
            assertEquals("Beta",  sorted.get(1).getTitle());
            assertEquals("Delta", sorted.get(2).getTitle());
            assertEquals("Gamma", sorted.get(3).getTitle());
        }

        @Test
        void sortByDueDate_chronologicalNullsLast() {
            List<Task> sorted = project.getSortedTasks(Project.TaskComparator.Criterion.DUE_DATE);
            assertEquals(D1,   sorted.get(0).getDueDate()); // "Beta"
            assertEquals(D2,   sorted.get(1).getDueDate()); // "Alpha"
            assertEquals(D3,   sorted.get(2).getDueDate()); // "Gamma"
            assertNull(         sorted.get(3).getDueDate()); // "Delta" — null last
        }

        @Test
        void sortByStatus_enumOrdinalOrder() {
            // TODO(0) < IN_PROGRESS(1) < DONE(2)
            List<Task> sorted = project.getSortedTasks(Project.TaskComparator.Criterion.STATUS);
            assertEquals(TaskStatus.TODO,        sorted.get(0).getStatus());
            assertEquals(TaskStatus.TODO,        sorted.get(1).getStatus());
            assertEquals(TaskStatus.IN_PROGRESS, sorted.get(2).getStatus());
            assertEquals(TaskStatus.DONE,        sorted.get(3).getStatus());
        }

        @Test
        void describe_containsProjectName() {
            Project.TaskComparator comp =
                    project.new TaskComparator(Project.TaskComparator.Criterion.TITLE);
            assertTrue(comp.describe().contains("TestProject"));
        }

        @Test
        void describe_containsCriterion() {
            Project.TaskComparator comp =
                    project.new TaskComparator(Project.TaskComparator.Criterion.STATUS);
            assertTrue(comp.describe().contains("STATUS"));
        }
    }

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Test
    void toString_containsName() {
        assertTrue(project.toString().contains("TestProject"));
    }

    @Test
    void toString_containsStatus() {
        assertTrue(project.toString().contains("To Do"));
    }

    @Test
    void toString_containsTaskCount() {
        project.addTask(taskA);
        project.addTask(taskB);
        assertTrue(project.toString().contains("2"));
    }
}
