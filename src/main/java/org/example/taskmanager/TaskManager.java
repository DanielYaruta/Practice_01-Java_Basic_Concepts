package org.example.taskmanager;

import org.example.Validate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TaskManager {

    private final List<Project> projects;

    public TaskManager() {
        this.projects = new ArrayList<>();
    }

    public void addProject(Project project) {
        Validate.requireNonNull(project, "project");
        projects.add(project);
        System.out.println("Project added: " + project.getName());
    }

    public boolean removeProject(Project project) {
        Validate.requireNonNull(project, "project");
        boolean removed = projects.remove(project);
        if (removed) System.out.println("Project removed: " + project.getName());
        return removed;
    }

    public Optional<Project> findByName(String name) {
        Validate.requireNonBlank(name, "name");
        return projects.stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public List<Project> getProjects() {
        return Collections.unmodifiableList(projects);
    }

    public void printAllProjects() {
        System.out.println("=== Projects ===");
        if (projects.isEmpty()) {
            System.out.println("  (no projects)");
            return;
        }
        projects.forEach(p -> {
            System.out.println("  " + p);
            p.printTasks();
        });
    }

    public ProjectStats getStats() {
        return new ProjectStats(projects);
    }

    public static class ProjectStats {

        private final int    totalProjects;
        private final int    totalTasks;
        private final int    todoTasks;
        private final int    inProgressTasks;
        private final int    completedTasks;

        ProjectStats(List<Project> projects) {
            this.totalProjects   = projects.size();
            this.totalTasks      = projects.stream().mapToInt(p -> p.getTasks().size()).sum();
            this.todoTasks       = sum(projects, TaskStatus.TODO);
            this.inProgressTasks = sum(projects, TaskStatus.IN_PROGRESS);
            this.completedTasks  = sum(projects, TaskStatus.DONE);
        }

        private static int sum(List<Project> projects, TaskStatus status) {
            return projects.stream()
                    .mapToInt(p -> (int) p.countByStatus(status))
                    .sum();
        }

        public int    getTotalProjects()   { return totalProjects; }
        public int    getTotalTasks()      { return totalTasks; }
        public int    getTodoTasks()       { return todoTasks; }
        public int    getInProgressTasks() { return inProgressTasks; }
        public int    getCompletedTasks()  { return completedTasks; }

        public double getCompletionRate() {
            return totalTasks == 0 ? 0.0 : (double) completedTasks / totalTasks * 100.0;
        }

        public void printReport() {
            System.out.println("=== Project Statistics ===");
            System.out.printf("  Total projects  : %d%n",   totalProjects);
            System.out.printf("  Total tasks     : %d%n",   totalTasks);
            System.out.printf("  To Do           : %d%n",   todoTasks);
            System.out.printf("  In Progress     : %d%n",   inProgressTasks);
            System.out.printf("  Completed       : %d%n",   completedTasks);
            System.out.printf("  Completion rate : %.1f%%%n", getCompletionRate());
        }
    }
}
