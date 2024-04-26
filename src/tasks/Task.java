package tasks;

public class Task {
    public String title;
    public String description;
    public final int taskId;
    public Statuses status;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.taskId = this.hashCode();
        this.status = Statuses.NEW;
    }


    public void setStatus(String status) {
        this.status = Statuses.valueOf(status);
    }

    public void ultimateSetter(String title, String description, String status) {
        this.title = title;
        this.description = description;
        this.status = Statuses.valueOf(status);
    }


}

enum Statuses {
    NEW,
    IN_PROGRESS,
    DONE
}

