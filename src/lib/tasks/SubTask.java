package lib.tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    public short epicId;

    public SubTask(
            String title,
            String description,
            String status,
            short epicId
    ) {
        super(title, description, status);
        setEpicIdAndTaskType(epicId);
    }

    public SubTask(short id,
                   String title,
                   String description,
                   String status,
                   short epicId
    ) {
        super(id, title, description, status);
        setEpicIdAndTaskType(epicId);
    }

    public SubTask(
            String title,
            String description,
            String status,
            short epicId,
            LocalDateTime startTime,
            Duration duration
    ) {
        super(title, description, status, startTime, duration);
        setEpicIdAndTaskType(epicId);

    }

    public SubTask(
            short id,
            String title,
            String description,
            String status,
            short epicId,
            LocalDateTime startTime,
            Duration duration
    ) {
        super(id, title, description, status, startTime, duration);
        setEpicIdAndTaskType(epicId);
    }

    private void setEpicIdAndTaskType(short epicId) {
        this.epicId = epicId;
        this.taskType = TaskType.SUBTASK;
    }

    @Override
    public SubTask clone() {
        return new SubTask(
                this.id,
                this.title,
                this.description,
                this.status.toString(),
                this.epicId,
                this.startTime,
                this.duration
        );
    }


}