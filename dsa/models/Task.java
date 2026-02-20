package models;

public class Task {
    public String id;
    public String title;
    public String subject;
    public int priority;
    public String deadline;
    public String status;

    public Task(String id, String title, String subject, int priority, String deadline, String status) {
        this.id = id;
        this.title = title;
        this.subject = subject;
        this.priority = priority;
        this.deadline = deadline;
        this.status = status;
    }
}