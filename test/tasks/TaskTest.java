package tasks;

import lib.tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

class TaskTest {
    private String title;
    private String description;
    private String status;

    @BeforeEach
    public void settingParametersForTask() {
        title = "Task_1";
        description = "Description_1";
        status = "NEW";
    }


    @Test
    public void constructorWithoutIdShouldWork() {
        Task task = new Task(title, description, status);

        boolean condition = (task.id > 0) && (task.title.equals(title)) && (task.description.equals(description)) &&
                (task.status.toString().equals(status));

        Assertions.assertTrue(condition);
    }

    @Test
    public void constructorWithIdShouldWork() {
        short id = 100;
        Task task = new Task(id, title, description, status);

        boolean condition = (task.id == id) && (task.title.equals(title)) && (task.description.equals(description)) &&
                (task.status.toString().equals(status));

        Assertions.assertTrue(condition);
    }

    @Test
    public void tasksShouldBeEqualWhenIdsAreEqual() {
        short id = 1;
        String titleSnd = "Task_2";
        String descriptionSnd = "Description_2";
        Task taskFst = new Task(id, title, description, status);
        Task taskSnd = new Task(id, titleSnd, descriptionSnd, status);

        Assertions.assertEquals(taskFst, taskSnd);
    }

    @Test
    public void hashCodeShouldBeEqualToId() {
        short id = 100;
        Task task = new Task(id, title, description, status);

        Assertions.assertEquals(task.id, task.hashCode());
    }

    @Test
    public void methodCloneShouldReturnNotSameObjectWithSameClass() {
        Task task = new Task(title, description, status);
        Task cloneTask = task.clone();

        boolean condition = (task != cloneTask) && (task.getClass() == cloneTask.getClass());

        Assertions.assertTrue(condition);
    }

    @Test
    public void possibleToSetTemporalProperties() {
        short id = 1;
        String stringStartTime = "2007-09-01T21:00";
        LocalDateTime startTime = LocalDateTime.parse(stringStartTime);
        int durationInMinutes = 60;
        Duration duration = Duration.ofMinutes(durationInMinutes);

        Task task = new Task(id, title, description, status, startTime, duration);

        Assertions.assertTrue(task.startTime.equals(startTime) && task.duration.equals(duration));
    }

    @Test
    public void collisionDetectsCorrectly() {
        Duration duration1 = Duration.ofHours(2);
        Duration duration2 = Duration.ofHours(4);
        Duration duration3 = Duration.ofHours(8);
        LocalDateTime startTime1 = LocalDateTime.of(2024, 1, 1, 12, 0);
        LocalDateTime startTime2 = startTime1.plus(duration1);
        LocalDateTime startTime3 = startTime1.plus(duration3);

        Task task1 = new Task("Task1", "Description1", "NEW", startTime1, duration2);
        Task task2 = new Task("Task2", "Description2", "NEW", startTime2, duration2);
        Task task3 = new Task("Task3", "Description3", "NEW", startTime3, duration2);

        boolean condition = task1.isTimeCollision(task2) && !task1.isTimeCollision(task3);
        Assertions.assertTrue(condition);
    }
}