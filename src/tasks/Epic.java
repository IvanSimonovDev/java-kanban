package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Short> subtasksIds;

    public Epic(String title, String description, String status) {
        super(title, description, status);
        this.createSubTasksStorage();
    }

    public Epic(short id, String title, String description, String status) {
        super(id, title, description, status);
        this.createSubTasksStorage();
    }

    private void createSubTasksStorage() {
        int initialCapacity = 7;
        this.subtasksIds = new ArrayList<>(initialCapacity);
    }

    @Override
    public Epic clone() {
        Epic result = new Epic(this.id, this.title, this.description, this.status.toString());
        result.subtasksIds = (ArrayList<Short>) this.subtasksIds.clone();
        return result;
    }

}
