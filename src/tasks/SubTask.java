package tasks;

public class SubTask extends Task {
    public Epic epicObject;

    public SubTask(String title, String description, String status, Epic epicObject) {
        super(title, description, status);

        this.epicObject = epicObject;
        epicObject.subTasksList.add(this);

    }

    public SubTask(String title, String description, String status, int taskId, Epic epicObject) {
        this(title, description, status, epicObject);
        this.taskId = taskId;
    }

}
