package web;

import lib.tasks.Epic;
import lib.tasks.SubTask;
import lib.tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class HistoryHandlerTest extends HandlersTest {

    // Testing endpoint GET /history
    @Test
    public void fourteenthEndPointShouldReturnEmptyHistoryWhenThereAreNoTasksOfAnyType()
            throws IOException, InterruptedException {
        Assertions.assertEquals(0, requestForHistory().length);
    }

    // Testing endpoint GET /history
    @Test
    public void fourteenthEndPointShouldReturnCorrectHistoryWhenThereAreSomeTasksOfAnyType()
            throws IOException, InterruptedException {
        Task task = TasksHandlerTest.createDefaultTask();
        requestToCreateOneTask(task);
        short taskId = requestForAllTasks().getFirst().id;

        Epic epic = SubtasksHandlerTest.createDefaultEpic();
        requestToCreateOneEpic(epic);
        short epicId = requestForAllEpics().getFirst().id;

        SubTask subtask1 = SubtasksHandlerTest.createDefaultSubTask(epicId);
        requestToCreateOneSubTask(subtask1);
        short subTaskId = requestForAllSubTasks().getFirst().id;

        requestForOneEpic(epicId);
        requestForOneSubTask(subTaskId);
        requestForOneTask(taskId);

        Task[] history = requestForHistory();

        boolean condition1 = history[0].id == taskId;
        boolean condition2 = history[1].id == subTaskId;
        boolean condition3 = history[2].id == epicId;

        Assertions.assertTrue(condition1 && condition2 && condition3);
    }

}
