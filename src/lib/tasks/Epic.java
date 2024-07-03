package lib.tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Short> subtasksIds;
    public LocalDateTime endTime;

    public Epic(String title, String description, String status) {
        super(title, description, status);
        this.createSubTasksStorageAndSetTaskType();
    }

    public Epic(short id, String title, String description, String status) {
        super(id, title, description, status);
        this.createSubTasksStorageAndSetTaskType();
    }

    private void createSubTasksStorageAndSetTaskType() {
        int initialCapacity = 7;
        this.subtasksIds = new ArrayList<>(initialCapacity);
        this.taskType = TaskType.EPIC;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public Epic clone() {
        Epic result = new Epic(this.id, this.title, this.description, this.status.toString());
        result.subtasksIds = (ArrayList<Short>) this.subtasksIds.clone();
        result.startTime = this.startTime;
        result.duration = this.duration;
        return result;
    }

}
