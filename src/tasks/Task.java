package tasks;

public class Task {
    public String title;
    public String description;
    public Statuses status;
    public  int taskId;

    public Task(String title, String description, String status) {
        this.title = title;
        this.description = description;
        this.status = Statuses.valueOf(status);
        this.taskId = this.hashCode();
    }

    public Task(String title, String description, String status,  int taskId) {
        this(title, description, status);
        this.taskId = taskId;
    }


    @Override
    public String toString() {
        String klass = this.getClass().toString().split("\\.")[1];
        return title + "( " + klass + ", id = " + taskId + " )";
    }

    public void setStatus(String status) {
        this.status = Statuses.valueOf(status);
    }
}

enum Statuses {
    NEW,
    IN_PROGRESS,
    DONE
}

