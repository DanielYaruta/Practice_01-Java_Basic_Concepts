package org.example.taskmanager;

public interface Manageable {
    void assign(String assignee);
    void start();
    void complete();
}
