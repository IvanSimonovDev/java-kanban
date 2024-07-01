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
}