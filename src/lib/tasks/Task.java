package lib.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class Task {
    private static final int INITIAL_CAPACITY = 20;
    private static final Random RANDOM_GENERATOR = new Random();
    public static final ArrayList<Short> ID_LIST = new ArrayList<>(INITIAL_CAPACITY);
    public short id;
    public String title;
    public String description;
    public Statuses status;
    public LocalDateTime startTime;
    public Duration duration;
    public TaskType taskType;

    public Task(
            String title,
            String description,
            String status
    ) {
        setTaskTypeAndCallMultipleSetter(title, description, status);

        short id = (short) RANDOM_GENERATOR.nextInt(Short.MAX_VALUE);
        while (ID_LIST.contains(id) || id == 0) {
            id = (short) RANDOM_GENERATOR.nextInt();
        }
        this.id = id;
        Task.ID_LIST.add(id);
    }

    public Task(
            short id,
            String title,
            String description,
            String status
    ) {
        setTaskTypeAndCallMultipleSetter(title, description, status);

        this.id = id;
    }

    public Task(
            String title,
            String description,
            String status,
            LocalDateTime startTime,
            Duration duration
    ) {
        this(title, description, status);
        this.startTime = startTime;
        this.duration = duration;

    }

    public Task(
            short id,
            String title,
            String description,
            String status,
            LocalDateTime startTime,
            Duration duration
    ) {
        this(title, description, status, startTime, duration);
        this.id = id;
    }

    private void setTaskTypeAndCallMultipleSetter(String title, String description, String status) {
        taskType = TaskType.TASK;
        multipleSetter(title, description, status);
    }

    private void multipleSetter(String title, String description, String status) {
        this.title = title;
        this.description = description;
        this.status = Statuses.valueOf(status);
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public boolean isTimeCollision(Task task) {
        if (this.areStartTimeAndDurationNotNull() && task.areStartTimeAndDurationNotNull()) {
            boolean condition1 = this.startTime.isAfter(task.getEndTime());
            boolean condition2 = this.getEndTime().isBefore(task.startTime);
            return !(condition1 || condition2);
        } else {
            return false;
        }
    }

    private boolean areStartTimeAndDurationNotNull() {
        return (this.startTime != null && this.duration != null);
    }


    @Override
    public String toString() {
        String klass = this.getClass().toString().split("\\.")[2];
        return title + " ( " + klass + ", id = " + id + " )";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if ((o == null) || (this.getClass() != o.getClass())) {
            return false;
        }
        Task task = (Task) o;

        return (this.id == task.id);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public Task clone() {
        return new Task(
                this.id,
                this.title,
                this.description,
                this.status.toString(),
                this.startTime,
                this.duration
        );
    }
}
