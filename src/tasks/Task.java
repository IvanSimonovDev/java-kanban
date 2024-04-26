package tasks;

public class Task {
    public String title;
    public String description;
    public short taskId;
    public Statuses status;
}

enum Statuses {
    NEW,
    IN_PROGRESS,
    DONE
}

