package web;

import lib.tasks.Epic;
import lib.tasks.SubTask;
import lib.tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class PrioritizedHandlerTest extends HandlersTest {

    // Testing endpoint GET /prioritized
    @Test
    public void fifteenthEndPointShouldReturnEmptyResultWhenThereAreNoTasksOfAnyType()
            throws IOException, InterruptedException {
        Assertions.assertEquals(0, requestForPrioritizedTasksAndSubtasks().length);
    }

    // Testing endpoint GET /prioritized
    @Test
    public void fifteenthEndPointShouldReturnTasksAndSubtasksInCorrectOrder()
            throws IOException, InterruptedException {
        Task task = TasksHandlerTest.createDefaultTask();
        requestToCreateOneTask(task);
        short taskId = requestForAllTasks().getFirst().id;

        Epic epic = SubtasksHandlerTest.createDefaultEpic();
        requestToCreateOneEpic(epic);
        short epicId = requestForAllEpics().getFirst().id;

        SubTask subtask1 = SubtasksHandlerTest.createDefaultSubTask(epicId);
        requestToCreateOneSubTask(subtask1);
        short fstSubTaskId = requestForAllSubTasks().getFirst().id;

        SubTask subtask2 = SubtasksHandlerTest.createAnotherDefaultSubTask(epicId);
        requestToCreateOneSubTask(subtask2);
        short sndSubTaskId = requestForAllSubTasks().getFirst().id;
        sndSubTaskId = (sndSubTaskId == fstSubTaskId) ? requestForAllSubTasks().getLast().id : sndSubTaskId;

        Task[] prioritizedTasksAndSubtasks = requestForPrioritizedTasksAndSubtasks();
        boolean condition1 = prioritizedTasksAndSubtasks[0].id == taskId;
        boolean condition2 = prioritizedTasksAndSubtasks[1].id == fstSubTaskId;
        boolean condition3 = prioritizedTasksAndSubtasks[2].id == sndSubTaskId;

        Assertions.assertTrue(condition1 && condition2 && condition3);
    }

}
