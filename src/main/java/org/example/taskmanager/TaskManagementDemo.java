package org.example.taskmanager;

import java.time.LocalDate;
import java.util.List;

public class TaskManagementDemo {

    public static void run() {
        TaskManager manager = new TaskManager();

        // --- Project 1: Backend API ---
        Project backend = new Project("Backend API", "REST API for mobile app");
        backend.addTask(new Task("Design DB schema",    "ERD + migrations",      LocalDate.now().minusDays(5)));
        backend.addTask(new Task("Implement auth",       "JWT + refresh tokens",  LocalDate.now().plusDays(3)));
        backend.addTask(new Task("Write unit tests",     "Cover service layer",   LocalDate.now().plusDays(7)));
        backend.addTask(new Task("Setup CI pipeline",    "GitHub Actions",        LocalDate.now().plusDays(1)));

        Task overdueTask = new Task("API documentation", "Swagger/OpenAPI", LocalDate.now().minusDays(2));
        overdueTask.setAssignee("Alice");
        backend.addTask(overdueTask);

        // --- Project 2: Mobile App ---
        Project mobile = new Project("Mobile App", "iOS/Android client");
        mobile.addTask(new Task("UI wireframes",    "Figma mockups",         LocalDate.now().plusDays(2)));
        mobile.addTask(new Task("Login screen",     "Auth flow integration", LocalDate.now().plusDays(5)));
        mobile.addTask(new Task("Push notifications", "FCM setup",           LocalDate.now().plusDays(10)));

        manager.addProject(backend);
        manager.addProject(mobile);

        System.out.println();
        manager.printAllProjects();

        // Manage backend project via Manageable
        System.out.println();
        backend.assign("Bob");
        backend.start();

        // Assign individual tasks
        backend.getTasks().get(0).setAssignee("Alice");
        backend.getTasks().get(0).setStatus(TaskStatus.DONE);
        backend.getTasks().get(1).setAssignee("Bob");
        backend.getTasks().get(1).setStatus(TaskStatus.IN_PROGRESS);
        backend.getTasks().get(3).setAssignee("Carol");

        // Manage mobile project
        mobile.assign("Diana");
        mobile.start();
        mobile.getTasks().get(0).setAssignee("Eve");
        mobile.getTasks().get(0).setStatus(TaskStatus.DONE);

        // Sort tasks by due date using inner TaskComparator
        System.out.println();
        List<Task> byDate = backend.getSortedTasks(Project.TaskComparator.Criterion.DUE_DATE);
        byDate.forEach(t -> System.out.println("    " + t));

        System.out.println();
        List<Task> byStatus = backend.getSortedTasks(Project.TaskComparator.Criterion.STATUS);
        byStatus.forEach(t -> System.out.println("    " + t));

        // Complete mobile project entirely
        System.out.println();
        mobile.complete();

        // Find project by name
        System.out.println();
        manager.findByName("backend api").ifPresent(p ->
                System.out.println("Found: " + p.getName() + " [" + p.getStatus() + "]"));

        // Final overview + stats
        System.out.println();
        manager.printAllProjects();

        System.out.println();
        manager.getStats().printReport();
    }
}
