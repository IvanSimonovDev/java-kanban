package lib.tasks;

public class SubTask extends Task {
    public short epicId;

    public SubTask(String title, String description, String status, short epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public SubTask(short id, String title, String description, String status, short epicId) {
        super(id, title, description, status);
        this.epicId = epicId;
    }

    @Override
    public SubTask clone() {
        return new SubTask(this.id, this.title, this.description, this.status.toString(), this.epicId);
    }


}