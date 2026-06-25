package org.example.taskmanager;

import org.example.Validate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Project implements Manageable {

    private String     name;
    private String     description;
    private TaskStatus status;
    private String     manager;
    private final List<Task> tasks;

    public Project(String name, String description) {
        this.name        = Validate.requireNonBlank(name, "name");
        this.description = Validate.requireNonNull(description, "description");
        this.status      = TaskStatus.TODO;
        this.tasks       = new ArrayList<>();
    }

    // --- Manageable ---

    @Override
    public void assign(String manager) {
        this.manager = Validate.requireNonBlank(manager, "manager");
        System.out.println("Project '" + name + "' assigned to: " + manager);
    }

    @Override
    public void start() {
        if (status == TaskStatus.TODO) {
            status = TaskStatus.IN_PROGRESS;
            System.out.println("Project '" + name + "' started.");
        } else {
            System.out.println("Project '" + name + "' is already " + status + ".");
        }
    }

    @Override
    public void complete() {
        tasks.forEach(t -> t.setStatus(TaskStatus.DONE));
        status = TaskStatus.DONE;
        System.out.println("Project '" + name + "' completed. All tasks marked as DONE.");
    }

    // --- Task management ---

    public void addTask(Task task) {
        Validate.requireNonNull(task, "task");
        tasks.add(task);
    }

    public boolean removeTask(Task task) {
        return tasks.remove(task);
    }

    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    public List<Task> getSortedTasks(TaskComparator.Criterion criterion) {
        TaskComparator comparator = new TaskComparator(criterion);
        System.out.println(comparator.describe());
        return tasks.stream().sorted(comparator).collect(Collectors.toList());
    }

    public long countByStatus(TaskStatus status) {
        return tasks.stream().filter(t -> t.getStatus() == status).count();
    }

    // --- Getters ---

    public String     getName()        { return name; }
    public String     getDescription() { return description; }
    public TaskStatus getStatus()      { return status; }
    public String     getManager()     { return manager; }

    public void printTasks() {
        System.out.println("  Tasks in '" + name + "':");
        if (tasks.isEmpty()) {
            System.out.println("    (no tasks)");
            return;
        }
        tasks.forEach(t -> System.out.println("    " + t));
    }

    @Override
    public String toString() {
        return String.format("%-22s | %-11s | tasks: %2d | manager: %s",
                name, status.getDisplayName(), tasks.size(),
                manager != null ? manager : "—");
    }

    // -------------------------------------------------------------------------
    // Non-static inner class — has implicit reference to the enclosing Project
    // -------------------------------------------------------------------------

    public class TaskComparator implements Comparator<Task> {

        public enum Criterion { DUE_DATE, STATUS, TITLE }

        private final Criterion criterion;

        public TaskComparator(Criterion criterion) {
            this.criterion = criterion;
        }

        @Override
        public int compare(Task a, Task b) {
            return switch (criterion) {
                case DUE_DATE -> Comparator.<LocalDate>nullsLast(Comparator.naturalOrder())
                        .compare(a.getDueDate(), b.getDueDate());
                case STATUS   -> a.getStatus().compareTo(b.getStatus());
                case TITLE    -> a.getTitle().compareToIgnoreCase(b.getTitle());
            };
        }

        // Accesses enclosing Project.this — demonstrates non-static inner class
        public String describe() {
            return "  [" + Project.this.name + "] sorting by " + criterion;
        }
    }
}
