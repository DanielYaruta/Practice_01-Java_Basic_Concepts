package org.example.taskmanager;

import org.example.Validate;

import java.time.LocalDate;

public class Task {

    private String     title;
    private String     description;
    private LocalDate  dueDate;
    private TaskStatus status;
    private String     assignee;

    public Task(String title, String description, LocalDate dueDate) {
        this.title       = Validate.requireNonBlank(title, "title");
        this.description = Validate.requireNonNull(description, "description");
        this.dueDate     = dueDate; // null = no deadline
        this.status      = TaskStatus.TODO;
    }

    public String     getTitle()       { return title; }
    public String     getDescription() { return description; }
    public LocalDate  getDueDate()     { return dueDate; }
    public TaskStatus getStatus()      { return status; }
    public String     getAssignee()    { return assignee; }

    public void setTitle(String title)             { this.title = Validate.requireNonBlank(title, "title"); }
    public void setDescription(String description) { this.description = Validate.requireNonNull(description, "description"); }
    public void setDueDate(LocalDate dueDate)      { this.dueDate = dueDate; } // null allowed
    public void setStatus(TaskStatus status)       { this.status = Validate.requireNonNull(status, "status"); }
    public void setAssignee(String assignee)       { this.assignee = assignee; } // null = unassign

    // --- final: business rules that must not change in subclasses ---

    public final boolean isOverdue() {
        return status != TaskStatus.DONE
                && dueDate != null
                && LocalDate.now().isAfter(dueDate);
    }

    public final boolean isCompleted() {
        return status == TaskStatus.DONE;
    }

    public final boolean isAssigned() {
        return assignee != null && !assignee.isBlank();
    }

    @Override
    public String toString() {
        String due    = dueDate != null ? dueDate.toString() : "no date";
        String owner  = isAssigned() ? assignee : "unassigned";
        String flag   = isOverdue() ? " [OVERDUE]" : "";
        return String.format("[%-11s] %-28s | due: %s | %s%s",
                status.getDisplayName(), title, due, owner, flag);
    }
}
