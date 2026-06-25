package org.example.taskmanager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {

    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream sink = new ByteArrayOutputStream();

    private TaskManager manager;
    private Project     alpha;
    private Project     beta;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(sink));
        manager = new TaskManager();
        alpha   = new Project("Alpha", "First project");
        beta    = new Project("Beta",  "Second project");
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    // -------------------------------------------------------------------------
    // addProject / removeProject
    // -------------------------------------------------------------------------

    @Test
    void addProject_appearsInList() {
        manager.addProject(alpha);
        assertTrue(manager.getProjects().contains(alpha));
    }

    @Test
    void addProject_multipleProjects_allPresent() {
        manager.addProject(alpha);
        manager.addProject(beta);
        assertEquals(2, manager.getProjects().size());
    }

    @Test
    void removeProject_existingProject_returnsTrue() {
        manager.addProject(alpha);
        assertTrue(manager.removeProject(alpha));
    }

    @Test
    void removeProject_existingProject_removedFromList() {
        manager.addProject(alpha);
        manager.removeProject(alpha);
        assertFalse(manager.getProjects().contains(alpha));
    }

    @Test
    void removeProject_notAdded_returnsFalse() {
        assertFalse(manager.removeProject(alpha));
    }

    @Test
    void getProjects_returnsUnmodifiableList() {
        manager.addProject(alpha);
        assertThrows(UnsupportedOperationException.class,
                () -> manager.getProjects().add(beta));
    }

    // -------------------------------------------------------------------------
    // findByName
    // -------------------------------------------------------------------------

    @Test
    void findByName_exactMatch_returnsProject() {
        manager.addProject(alpha);
        Optional<Project> result = manager.findByName("Alpha");
        assertTrue(result.isPresent());
        assertSame(alpha, result.get());
    }

    @Test
    void findByName_caseInsensitive_returnsProject() {
        manager.addProject(alpha);
        assertTrue(manager.findByName("alpha").isPresent());
        assertTrue(manager.findByName("ALPHA").isPresent());
        assertTrue(manager.findByName("AlPhA").isPresent());
    }

    @Test
    void findByName_noMatch_returnsEmpty() {
        manager.addProject(alpha);
        assertFalse(manager.findByName("Gamma").isPresent());
    }

    @Test
    void findByName_emptyManager_returnsEmpty() {
        assertFalse(manager.findByName("Alpha").isPresent());
    }

    // -------------------------------------------------------------------------
    // ProjectStats
    // -------------------------------------------------------------------------

    @Nested
    class ProjectStatsTest {

        @Test
        void emptyManager_allStatsZero() {
            TaskManager.ProjectStats stats = manager.getStats();
            assertEquals(0, stats.getTotalProjects());
            assertEquals(0, stats.getTotalTasks());
            assertEquals(0, stats.getTodoTasks());
            assertEquals(0, stats.getInProgressTasks());
            assertEquals(0, stats.getCompletedTasks());
            assertEquals(0.0, stats.getCompletionRate(), 0.001);
        }

        @Test
        void totalProjects_countsCorrectly() {
            manager.addProject(alpha);
            manager.addProject(beta);
            assertEquals(2, manager.getStats().getTotalProjects());
        }

        @Test
        void totalTasks_sumsAcrossProjects() {
            alpha.addTask(new Task("T1", "", LocalDate.of(2099, 1, 1)));
            alpha.addTask(new Task("T2", "", LocalDate.of(2099, 1, 2)));
            beta.addTask(new Task("T3",  "", LocalDate.of(2099, 1, 3)));
            manager.addProject(alpha);
            manager.addProject(beta);
            assertEquals(3, manager.getStats().getTotalTasks());
        }

        @Test
        void todoTasks_countedCorrectly() {
            Task t1 = new Task("T1", "", null); // TODO
            Task t2 = new Task("T2", "", null); // TODO
            Task t3 = new Task("T3", "", null);
            t3.setStatus(TaskStatus.IN_PROGRESS);
            alpha.addTask(t1);
            alpha.addTask(t2);
            alpha.addTask(t3);
            manager.addProject(alpha);
            assertEquals(2, manager.getStats().getTodoTasks());
        }

        @Test
        void inProgressTasks_countedCorrectly() {
            Task t1 = new Task("T1", "", null);
            t1.setStatus(TaskStatus.IN_PROGRESS);
            Task t2 = new Task("T2", "", null);
            t2.setStatus(TaskStatus.IN_PROGRESS);
            alpha.addTask(t1);
            beta.addTask(t2);
            manager.addProject(alpha);
            manager.addProject(beta);
            assertEquals(2, manager.getStats().getInProgressTasks());
        }

        @Test
        void completedTasks_countedCorrectly() {
            Task t1 = new Task("T1", "", null);
            t1.setStatus(TaskStatus.DONE);
            Task t2 = new Task("T2", "", null);
            alpha.addTask(t1);
            alpha.addTask(t2);
            manager.addProject(alpha);
            assertEquals(1, manager.getStats().getCompletedTasks());
        }

        @Test
        void completionRate_allDone_is100() {
            Task t1 = new Task("T1", "", null);
            t1.setStatus(TaskStatus.DONE);
            Task t2 = new Task("T2", "", null);
            t2.setStatus(TaskStatus.DONE);
            alpha.addTask(t1);
            alpha.addTask(t2);
            manager.addProject(alpha);
            assertEquals(100.0, manager.getStats().getCompletionRate(), 0.001);
        }

        @Test
        void completionRate_noneDone_isZero() {
            alpha.addTask(new Task("T1", "", null));
            alpha.addTask(new Task("T2", "", null));
            manager.addProject(alpha);
            assertEquals(0.0, manager.getStats().getCompletionRate(), 0.001);
        }

        @Test
        void completionRate_halfDone_is50() {
            Task t1 = new Task("T1", "", null);
            t1.setStatus(TaskStatus.DONE);
            Task t2 = new Task("T2", "", null);
            alpha.addTask(t1);
            alpha.addTask(t2);
            manager.addProject(alpha);
            assertEquals(50.0, manager.getStats().getCompletionRate(), 0.001);
        }

        @Test
        void stats_isSnapshot_notAffectedByLaterChanges() {
            alpha.addTask(new Task("T1", "", null));
            manager.addProject(alpha);
            TaskManager.ProjectStats snapshot = manager.getStats();
            // add more tasks after snapshot
            alpha.addTask(new Task("T2", "", null));
            // snapshot must reflect the state at the time getStats() was called
            assertEquals(1, snapshot.getTotalTasks());
        }
    }
}
