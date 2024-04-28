package tasks;

public class SubTask extends Task{
    public short epicId;

    public SubTask(String title, String description, String status, short epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public SubTask(short id, String title, String description, String status, short epicId) {
        super(id, title, description, status);
        this.epicId = epicId;
    }


}