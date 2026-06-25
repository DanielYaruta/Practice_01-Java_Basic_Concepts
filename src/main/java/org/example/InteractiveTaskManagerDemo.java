package org.example;

import org.example.taskmanager.*;
import org.example.util.InputHelper;
import org.example.util.InputHelperException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class InteractiveTaskManagerDemo {

    private final InputHelper input;
    private final TaskManager manager = new TaskManager();

    public InteractiveTaskManagerDemo(InputHelper input) {
        this.input = input;
    }

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║  Task Manager — Interactive Demo     ║");
        System.out.println("╚══════════════════════════════════════╝\n");

        try (InputHelper ih = new InputHelper(System.in, System.out, 3)) {
            new InteractiveTaskManagerDemo(ih).run();
        } catch (InputHelperException e) {
            System.err.println("\n[!] Слишком много неверных вводов: " + e.getMessage());
        }
    }

    public void run() {
        mainMenu();
    }

    private void mainMenu() {
        boolean running = true;
        while (running) {
            System.out.println("━━━ Task Manager ━━━");
            System.out.println("  1. Список проектов");
            System.out.println("  2. Новый проект");
            System.out.println("  3. Открыть проект");
            System.out.println("  4. Найти проект по названию");
            System.out.println("  5. Статистика");
            System.out.println("  0. Выход");

            int choice = input.readInt("Выбор: ", 0, 5);
            System.out.println();
            switch (choice) {
                case 1 -> listProjects();
                case 2 -> addProject();
                case 3 -> selectAndOpenProject();
                case 4 -> findProject();
                case 5 -> manager.getStats().printReport();
                case 0 -> { System.out.println("До свидания!"); running = false; }
            }
            if (choice != 0) System.out.println();
        }
    }

    private void listProjects() {
        List<Project> projects = manager.getProjects();
        if (projects.isEmpty()) {
            System.out.println("Проектов нет. Создайте первый (пункт 2).");
            return;
        }
        System.out.println("─── Проекты ───");
        for (int i = 0; i < projects.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1, projects.get(i));
        }
    }

    private void addProject() {
        String name = input.readNonBlank("Название проекта: ");
        String desc = input.readLine("Описание (Enter — пропустить): ").trim();

        Project project = new Project(name, desc);

        if (input.readBoolean("Назначить менеджера сейчас?")) {
            project.assign(input.readNonBlank("Имя менеджера: "));
        }

        manager.addProject(project);
    }

    private void selectAndOpenProject() {
        List<Project> projects = manager.getProjects();
        if (projects.isEmpty()) {
            System.out.println("Проектов нет. Сначала создайте проект.");
            return;
        }
        listProjects();
        int idx = input.readInt("Выберите проект: ", 1, projects.size()) - 1;
        projectMenu(projects.get(idx));
    }

    private void findProject() {
        String name = input.readNonBlank("Название для поиска: ");
        manager.findByName(name).ifPresentOrElse(
                p -> {
                    System.out.println("Найден: " + p);
                    if (input.readBoolean("Открыть проект?")) {
                        System.out.println();
                        projectMenu(p);
                    }
                },
                () -> System.out.println("Проект не найден: \"" + name + "\"")
        );
    }

    private void projectMenu(Project project) {
        boolean open = true;
        while (open) {
            printProjectHeader(project);
            System.out.println("  1. Список задач");
            System.out.println("  2. Добавить задачу");
            System.out.println("  3. Открыть задачу");
            System.out.println("  4. Сортировать задачи");
            System.out.println("  5. Запустить проект");
            System.out.println("  6. Завершить проект");
            System.out.println("  7. Назначить/сменить менеджера");
            System.out.println("  8. Удалить проект");
            System.out.println("  0. Назад");

            int choice = input.readInt("Выбор: ", 0, 8);
            System.out.println();
            switch (choice) {
                case 1 -> project.printTasks();
                case 2 -> addTask(project);
                case 3 -> selectAndOpenTask(project);
                case 4 -> sortTasks(project);
                case 5 -> project.start();
                case 6 -> confirmAndComplete(project);
                case 7 -> reassignManager(project);
                case 8 -> { if (removeProject(project)) open = false; }
                case 0 -> open = false;
            }
            if (choice != 0 && open) System.out.println();
        }
    }

    private void printProjectHeader(Project project) {
        System.out.printf("━━━ Проект: %-22s [%s]  задач: %d ━━━%n",
                project.getName(), project.getStatus(), project.getTasks().size());
        if (project.getManager() != null) {
            System.out.println("    Менеджер: " + project.getManager());
        }
    }

    private void addTask(Project project) {
        String title = input.readNonBlank("Название задачи: ");
        String desc  = input.readLine("Описание (Enter — пропустить): ").trim();
        LocalDate due = readDate("Дедлайн ГГГГ-ММ-ДД (Enter — без дедлайна): ");

        Task task = new Task(title, desc, due);

        if (input.readBoolean("Назначить исполнителя?")) {
            task.setAssignee(input.readNonBlank("Имя исполнителя: "));
        }

        project.addTask(task);
        System.out.println("Добавлена: " + task);
    }

    private void selectAndOpenTask(Project project) {
        List<Task> tasks = project.getTasks();
        if (tasks.isEmpty()) {
            System.out.println("Задач нет. Добавьте первую (пункт 2).");
            return;
        }
        System.out.println("─── Задачи: " + project.getName() + " ───");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1, tasks.get(i));
        }
        int idx = input.readInt("Выберите задачу: ", 1, tasks.size()) - 1;
        System.out.println();
        taskMenu(project, tasks.get(idx));
    }

    private void sortTasks(Project project) {
        if (project.getTasks().isEmpty()) {
            System.out.println("Нет задач для сортировки.");
            return;
        }
        Project.TaskComparator.Criterion criterion =
                input.readEnum("Сортировать по:", Project.TaskComparator.Criterion.class);
        List<Task> sorted = project.getSortedTasks(criterion);
        System.out.println("─── Результат ───");
        sorted.forEach(t -> System.out.println("  " + t));
    }

    private void confirmAndComplete(Project project) {
        long pending = project.countByStatus(TaskStatus.TODO)
                     + project.countByStatus(TaskStatus.IN_PROGRESS);
        if (pending > 0) {
            System.out.printf("Внимание: %d задач будут помечены как DONE.%n", pending);
        }
        if (input.readBoolean("Завершить проект «" + project.getName() + "»?")) {
            project.complete();
        } else {
            System.out.println("Отменено.");
        }
    }

    private void reassignManager(Project project) {
        if (project.getManager() != null) {
            System.out.println("Текущий менеджер: " + project.getManager());
        }
        project.assign(input.readNonBlank("Новый менеджер: "));
    }

    private boolean removeProject(Project project) {
        if (input.readBoolean("Удалить проект «" + project.getName() + "»?")) {
            manager.removeProject(project);
            return true;
        }
        System.out.println("Отменено.");
        return false;
    }

    private void taskMenu(Project project, Task task) {
        boolean open = true;
        while (open) {
            System.out.println("━━━ Задача ━━━");
            System.out.println("  " + task);
            System.out.println("  1. Изменить статус");
            System.out.println("  2. Назначить исполнителя");
            System.out.println("  3. Снять исполнителя");
            System.out.println("  4. Изменить дедлайн");
            System.out.println("  5. Удалить задачу");
            System.out.println("  0. Назад");

            int choice = input.readInt("Действие: ", 0, 5);
            System.out.println();
            switch (choice) {
                case 1 -> changeTaskStatus(task);
                case 2 -> assignTask(task);
                case 3 -> { task.setAssignee(null); System.out.println("Исполнитель снят."); }
                case 4 -> changeTaskDueDate(task);
                case 5 -> { if (removeTask(project, task)) open = false; }
                case 0 -> open = false;
            }
            if (choice != 0 && open) System.out.println();
        }
    }

    private void changeTaskStatus(Task task) {
        TaskStatus status = input.readEnum("Новый статус:", TaskStatus.class);
        task.setStatus(status);
        System.out.println("Статус → " + status);
        if (task.isOverdue()) System.out.println("  ⚠ Задача просрочена!");
    }

    private void assignTask(Task task) {
        if (task.isAssigned()) {
            System.out.println("Текущий исполнитель: " + task.getAssignee());
        }
        task.setAssignee(input.readNonBlank("Имя исполнителя: "));
        System.out.println("Назначен: " + task.getAssignee());
    }

    private void changeTaskDueDate(Task task) {
        System.out.println("Текущий дедлайн: "
                + (task.getDueDate() != null ? task.getDueDate() : "не задан"));
        LocalDate due = readDate("Новый дедлайн ГГГГ-ММ-ДД (Enter — убрать): ");
        task.setDueDate(due);
        System.out.println("Дедлайн → " + (due != null ? due : "не задан"));
    }

    private boolean removeTask(Project project, Task task) {
        if (input.readBoolean("Удалить задачу «" + task.getTitle() + "»?")) {
            project.removeTask(task);
            System.out.println("Задача удалена.");
            return true;
        }
        System.out.println("Отменено.");
        return false;
    }

    // InputHelper не знает о LocalDate — используем readLine + ручной DateTimeParseException.
    private LocalDate readDate(String prompt) {
        while (true) {
            String raw = input.readLine(prompt).trim();
            if (raw.isEmpty()) return null;
            try {
                return LocalDate.parse(raw);
            } catch (DateTimeParseException e) {
                System.out.println("  Error: неверный формат — используйте ГГГГ-ММ-ДД.");
            }
        }
    }
}
