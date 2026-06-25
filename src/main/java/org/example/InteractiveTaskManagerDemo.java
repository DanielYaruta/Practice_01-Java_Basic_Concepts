package org.example;

import org.example.taskmanager.*;
import org.example.util.InputHelper;
import org.example.util.InputHelperException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Интерактивное демо системы управления задачами.
 *
 * InputHelper-методы, задействованные здесь:
 *   readNonBlank  — названия проектов/задач, имена менеджеров/исполнителей
 *   readLine      — описания (могут быть пустыми)
 *   readInt(min,max) — пункты меню, выбор проекта/задачи
 *   readEnum      — TaskStatus (To Do / In Progress / Done)
 *                   TaskComparator.Criterion (DUE_DATE / STATUS / TITLE)
 *   readBoolean   — подтверждение удаления, назначения, завершения проекта
 *
 * readDate() — демонстрирует комбинирование readLine с ручной валидацией,
 * когда InputHelper не знает о специфическом формате (ГГГГ-ММ-ДД).
 */
public class InteractiveTaskManagerDemo {

    private final InputHelper  input;
    private final TaskManager  manager = new TaskManager();

    public InteractiveTaskManagerDemo(InputHelper input) {
        this.input = input;
    }

    // ── Точка входа ────────────────────────────────────────────────────────────

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

    // ══════════════════════════════════════════════════════════════════════════
    // Главное меню
    // ══════════════════════════════════════════════════════════════════════════

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

    // ── 1. Список проектов ─────────────────────────────────────────────────────

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

    // ── 2. Новый проект ────────────────────────────────────────────────────────

    private void addProject() {
        // readNonBlank: название обязательно
        String name = input.readNonBlank("Название проекта: ");

        // readLine: описание может быть пустым
        String desc = input.readLine("Описание (Enter — пропустить): ").trim();

        Project project = new Project(name, desc);

        // readBoolean: нужен ли менеджер сразу?
        if (input.readBoolean("Назначить менеджера сейчас?")) {
            // readNonBlank: имя менеджера не может быть пустым
            String mgr = input.readNonBlank("Имя менеджера: ");
            project.assign(mgr);
        }

        manager.addProject(project);
    }

    // ── 3. Открыть проект (выбор из списка) ───────────────────────────────────

    private void selectAndOpenProject() {
        List<Project> projects = manager.getProjects();
        if (projects.isEmpty()) {
            System.out.println("Проектов нет. Сначала создайте проект.");
            return;
        }
        listProjects();

        // readInt с диапазоном: нельзя выбрать несуществующий номер
        int idx = input.readInt("Выберите проект: ", 1, projects.size()) - 1;
        projectMenu(projects.get(idx));
    }

    // ── 4. Найти по названию ───────────────────────────────────────────────────

    private void findProject() {
        // readNonBlank: пустой поисковой запрос недопустим
        String name = input.readNonBlank("Название для поиска: ");

        manager.findByName(name).ifPresentOrElse(
                p -> {
                    System.out.println("Найден: " + p);
                    // readBoolean: сразу открыть?
                    if (input.readBoolean("Открыть проект?")) {
                        System.out.println();
                        projectMenu(p);
                    }
                },
                () -> System.out.println("Проект не найден: \"" + name + "\"")
        );
    }

    // ══════════════════════════════════════════════════════════════════════════
    // Меню проекта
    // ══════════════════════════════════════════════════════════════════════════

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

    // ── 2. Добавить задачу ─────────────────────────────────────────────────────

    private void addTask(Project project) {
        // readNonBlank: название задачи
        String title = input.readNonBlank("Название задачи: ");

        // readLine: описание (может быть пустым)
        String desc = input.readLine("Описание (Enter — пропустить): ").trim();

        // readDate: кастомный метод с ручным циклом на основе readLine
        LocalDate due = readDate("Дедлайн ГГГГ-ММ-ДД (Enter — без дедлайна): ");

        Task task = new Task(title, desc, due);

        // readBoolean + readNonBlank: назначение исполнителя
        if (input.readBoolean("Назначить исполнителя?")) {
            String assignee = input.readNonBlank("Имя исполнителя: ");
            task.setAssignee(assignee);
        }

        project.addTask(task);
        System.out.println("Добавлена: " + task);
    }

    // ── 3. Открыть задачу ─────────────────────────────────────────────────────

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

    // ── 4. Сортировать задачи ─────────────────────────────────────────────────

    private void sortTasks(Project project) {
        if (project.getTasks().isEmpty()) {
            System.out.println("Нет задач для сортировки.");
            return;
        }
        // readEnum: Criterion — DUE_DATE, STATUS, TITLE
        Project.TaskComparator.Criterion criterion =
                input.readEnum("Сортировать по:", Project.TaskComparator.Criterion.class);

        List<Task> sorted = project.getSortedTasks(criterion);
        System.out.println("─── Результат ───");
        sorted.forEach(t -> System.out.println("  " + t));
    }

    // ── 6. Завершить проект ───────────────────────────────────────────────────

    private void confirmAndComplete(Project project) {
        long pending = project.countByStatus(TaskStatus.TODO)
                     + project.countByStatus(TaskStatus.IN_PROGRESS);
        if (pending > 0) {
            System.out.printf("Внимание: %d задач будут помечены как DONE.%n", pending);
        }
        // readBoolean: предупреждение перед деструктивным действием
        if (input.readBoolean("Завершить проект «" + project.getName() + "»?")) {
            project.complete();
        } else {
            System.out.println("Отменено.");
        }
    }

    // ── 7. Назначить/сменить менеджера ────────────────────────────────────────

    private void reassignManager(Project project) {
        if (project.getManager() != null) {
            System.out.println("Текущий менеджер: " + project.getManager());
        }
        // readNonBlank: пустое имя недопустимо
        String mgr = input.readNonBlank("Новый менеджер: ");
        project.assign(mgr);
    }

    // ── 8. Удалить проект ─────────────────────────────────────────────────────

    private boolean removeProject(Project project) {
        if (input.readBoolean("Удалить проект «" + project.getName() + "»?")) {
            manager.removeProject(project);
            return true;
        }
        System.out.println("Отменено.");
        return false;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // Меню задачи
    // ══════════════════════════════════════════════════════════════════════════

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
        // readEnum: показывает "To Do / In Progress / Done"
        TaskStatus status = input.readEnum("Новый статус:", TaskStatus.class);
        task.setStatus(status);
        System.out.println("Статус → " + status);
        if (task.isOverdue()) System.out.println("  ⚠ Задача просрочена!");
    }

    private void assignTask(Task task) {
        if (task.isAssigned()) {
            System.out.println("Текущий исполнитель: " + task.getAssignee());
        }
        // readNonBlank: имя исполнителя
        String name = input.readNonBlank("Имя исполнителя: ");
        task.setAssignee(name);
        System.out.println("Назначен: " + name);
    }

    private void changeTaskDueDate(Task task) {
        System.out.println("Текущий дедлайн: "
                + (task.getDueDate() != null ? task.getDueDate() : "не задан"));

        // readDate: кастомный метод — Enter очищает дедлайн
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

    // ══════════════════════════════════════════════════════════════════════════
    // readDate — readLine + ручная валидация формата
    //
    // InputHelper не знает о LocalDate, поэтому комбинируем:
    //   readLine — получаем сырую строку без retry
    //   ручной цикл — повторяем при DateTimeParseException
    //   пустая строка — означает «без дедлайна»
    // ══════════════════════════════════════════════════════════════════════════

    private LocalDate readDate(String prompt) {
        while (true) {
            String raw = input.readLine(prompt).trim();
            if (raw.isEmpty()) return null;
            try {
                return LocalDate.parse(raw);      // ожидает ISO: 2025-12-31
            } catch (DateTimeParseException e) {
                System.out.println("  Error: неверный формат — используйте ГГГГ-ММ-ДД.");
            }
        }
    }
}
