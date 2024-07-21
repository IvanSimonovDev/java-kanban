package web;

import com.google.gson.Gson;
import lib.managers.Managers;
import lib.managers.TaskManager;
import lib.tasks.Epic;
import lib.tasks.SubTask;
import lib.web.handlers.BaseHttpHandler;
import lib.web.handlers.EpicsHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EpicsHandlerTest extends HandlersTest {
    BaseHttpHandler handler;

    @Test
    // Testing endpoint GET /epics
    public void tenthEndPointShouldWorkCorrectlyWhenThereAreNoEpics() throws IOException, InterruptedException {
        List<Epic> listOfEpics = requestForAllEpics();

        boolean condition1 = httpResponse.statusCode() == 200;
        boolean condition2 = listOfEpics.isEmpty();
        Assertions.assertTrue(condition1 && condition2);
    }

    @Test
    // Testing endpoint DELETE /epics/:id
    public void eleventhEndPointShouldWorkCorrectly() throws IOException, InterruptedException {
        Epic epic = SubtasksHandlerTest.createDefaultEpic();
        requestToCreateOneEpic(epic);

        short idForRequest = requestForAllEpics().getFirst().id;
        requestToDeleteOneEpic(idForRequest);

        Assertions.assertTrue(requestForAllEpics().isEmpty());
    }

    // Testing endpoint GET /epics/:id
    @Test
    public void twelfthEndPointShouldWorkCorrectly() throws IOException, InterruptedException {
        Epic epic = SubtasksHandlerTest.createDefaultEpic();
        requestToCreateOneEpic(epic);

        short idForRequest = requestForAllEpics().getFirst().id;
        Epic returnedEpic = requestForOneEpic(idForRequest);

        Assertions.assertEquals(epic.title, returnedEpic.title);
    }

    // Testing endpoint GET /epics/:id/subtasks
    @Test
    public void thirteenthEndPointShouldWorkCorrectly() throws IOException, InterruptedException {
        Epic epic = SubtasksHandlerTest.createDefaultEpic();
        requestToCreateOneEpic(epic);

        short epicId = requestForAllEpics().getFirst().id;
        SubTask subTask1 = SubtasksHandlerTest.createDefaultSubTask(epicId);
        requestToCreateOneSubTask(subTask1);
        SubTask subTask2 = SubtasksHandlerTest.createAnotherDefaultSubTask(epicId);
        requestToCreateOneSubTask(subTask2);

        Set<SubTask> subTasksOfEpic = new HashSet<>(requestForAllSubTasksOfEpic(epicId));
        Set<SubTask> allSubTasks = new HashSet<>(requestForAllSubTasks());

        Assertions.assertEquals(subTasksOfEpic, allSubTasks);
    }

    @Test
    public void pathCheckShouldWorkCorrect() {
        initializeHandler();
        boolean condition1 = handler.isPathCorrect("/epics");
        boolean condition2 = handler.isPathCorrect("/epics/3");
        boolean condition3 = handler.isPathCorrect("/epics/fsff");
        boolean condition4 = handler.isPathCorrect("/epics/3/subtasks");
        boolean condition5 = handler.isPathCorrect("/epics/3/ds");
        boolean condition6 = handler.isPathCorrect("/epics/3/3");
        boolean condition7 = handler.isPathCorrect("/epics/fsff/subtasks");
        boolean condition8 = handler.isPathCorrect("/epics/fsff/ds");
        boolean condition9 = handler.isPathCorrect("/epics/fsff/3");
        boolean condition10 = handler.isPathCorrect("/epics/3/subtasks/4");


        boolean condition = condition1 &&
                condition2 &&
                !condition3 &&
                condition4 &&
                !condition5 &&
                !condition6 &&
                !condition7 &&
                !condition8 &&
                !condition9 &&
                !condition10;

        Assertions.assertTrue(condition);
    }

    public void initializeHandler() {
        TaskManager tasksManager = Managers.getDefaultFileBackedTaskManager();
        Gson gson = Managers.getDefaultGson();
        handler = new EpicsHandler(tasksManager, gson);
    }
}
