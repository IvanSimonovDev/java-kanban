import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lib.tasks.Epic;
import lib.tasks.SubTask;
import lib.tasks.Task;
import lib.web.typeAdapters.DurationAdapter;
import lib.web.typeAdapters.LocalDateTimeAdapter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GsonTest {
    private static Gson gson;

    @BeforeEach
    public void gsonInitialize() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.setPrettyPrinting()
                          .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                          .registerTypeAdapter(Duration.class, new DurationAdapter())
                          .create();
    }

    @Test
    public void taskShouldConvertCorrectly() {
        Task task = new Task((short) 1, "Task_1", "Description of Task_1", "NEW",
                LocalDateTime.parse("2024-11-11T13:00"), Duration.ofMinutes(33));
        String taskInJson = gson.toJson(task);
        Task taskCopy = gson.fromJson(taskInJson, Task.class);
        Assertions.assertTrue(tasksHaveSameFields(task, taskCopy));
    }

    @Test
    public void subtaskShouldConvertCorrectly() {
        Epic epic = new Epic((short) 1, "Epic_1", "Description_1", "NEW");

        //creating subtask
        SubTask subtask = new SubTask(
                (short) 2,
                "Subtask_1",
                "Description_2",
                "NEW",
                epic.id,
                LocalDateTime.parse("2007-09-01T21:00"),
                Duration.ofMinutes(60)
        );

        String subtaskInJson = gson.toJson(subtask);
        SubTask subtaskCopy = gson.fromJson(subtaskInJson, SubTask.class);
        boolean condition1 = tasksHaveSameFields(subtask, subtaskCopy);
        boolean condition2 = subtask.epicId == subtaskCopy.epicId;
        Assertions.assertTrue(condition1 && condition2);
    }

    @Test
    public void fullSetEpicShouldConvertCorrectly() {
        //creating Epic
        Epic epic = new Epic((short) 1, "Epic_1", "Description_1", "NEW");
        epic.startTime = LocalDateTime.parse("2007-09-01T21:00");
        epic.duration = Duration.ofMinutes(60);
        epic.endTime = LocalDateTime.parse("2008-09-01T21:00");
        List<Short> supportList = List.of((short) 5, (short) 6);
        epic.subtasksIds = new ArrayList<>(supportList);



        String epicInJson = gson.toJson(epic);
        Epic epicCopy = gson.fromJson(epicInJson, Epic.class);
        boolean condition1 = tasksHaveSameFields(epic, epicCopy);
        boolean condition2 = epic.endTime.equals(epicCopy.endTime) && epic.subtasksIds.equals(epicCopy.subtasksIds);
        Assertions.assertTrue(condition1 && condition2);
    }

    @Test
    public void partiallySetEpicShouldConvertCorrectly() {
        //creating Epic
        Epic epic = new Epic((short) 1, "Epic_1", "Description_1", "NEW");

        String epicInJson = gson.toJson(epic);
        Epic epicCopy = gson.fromJson(epicInJson, Epic.class);
        boolean condition1 = tasksHaveSomeSameFields(epic, epicCopy);
        boolean condition2 = epic.startTime == epicCopy.startTime && epic.duration == epicCopy.duration;
        boolean condition3 = epic.endTime == epicCopy.endTime && epic.subtasksIds.equals(epicCopy.subtasksIds);
        Assertions.assertTrue(condition1 && condition2 && condition3);
    }

    public boolean tasksHaveSameFields(Task task1, Task task2) {
        boolean condition = tasksHaveSomeSameFields(task1, task2);
        boolean condition6 = task1.startTime.equals(task2.startTime);
        boolean condition7 = task1.duration.equals(task2.duration);
        return condition && condition6 && condition7;
    }

    public boolean tasksHaveSomeSameFields(Task task1, Task task2) {
        boolean condition1 = task1.id == task2.id;
        boolean condition2 = task1.title.equals(task2.title);
        boolean condition3 = task1.description.equals(task2.description);
        boolean condition4 = task1.status == task2.status;
        boolean condition5 = task1.taskType == task2.taskType;
        return condition1 && condition2 && condition3 && condition4 && condition5;
    }
}
