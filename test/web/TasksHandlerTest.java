package web;

import com.google.gson.Gson;
import lib.managers.Managers;
import lib.managers.TaskManager;
import lib.tasks.Task;
import lib.web.HttpTaskServer;
import lib.web.handlers.BaseHttpHandler;
import lib.web.handlers.TasksHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class TasksHandlerTest extends HandlersTest {
    static BaseHttpHandler handler;

    @Test
    // Testing endpoint GET /tasks
    public void firstEndPointShouldWorkCorrectlyWhenThereAreNoTasks() throws IOException, InterruptedException {
        List<Task> listOfTasks = requestForAllTasks();

        boolean condition1 = httpResponse.statusCode() == 200;
        boolean condition2 = listOfTasks.isEmpty();
        Assertions.assertTrue(condition1 && condition2);
    }

    @Test
    // Testing endpoint Post /tasks
    public void secondEndPointShouldWorkCorrectlyWhenCreating() throws IOException, InterruptedException {
        Task task = createDefaultTask();
        requestToCreateOneTask(task);

        boolean condition1 = httpResponse.statusCode() == 201;
        boolean condition2 = httpResponse.body().isEmpty();
        boolean condition3 = requestForAllTasks().getFirst().title.equals(task.title);
        boolean condition4 = requestForAllTasks().size() == 1;
        Assertions.assertTrue(condition1 && condition2 && condition3 && condition4);
    }

    @Test
    // Testing endpoint GET /tasks/:id
    public void thirdEndPointShouldWorkCorrectly() throws IOException, InterruptedException {
        Task task = createDefaultTask();
        requestToCreateOneTask(task);

        short idForRequest = requestForAllTasks().getFirst().id;
        Task returnedTask = requestForOneTask(idForRequest);

        Assertions.assertEquals(task.title, returnedTask.title);
    }

    @Test
    // Testing endpoint DELETE /tasks/:id
    public void forthEndPointShouldWorkCorrectly() throws IOException, InterruptedException {
        Task task = createDefaultTask();
        requestToCreateOneTask(task);

        short idForRequest = requestForAllTasks().getFirst().id;
        requestToDeleteOneTask(idForRequest);

        Assertions.assertTrue(requestForAllTasks().isEmpty());
    }

    @Test
    public void pathCheckShouldWorkCorrect() {
        initializeHandler();
        boolean condition1 = handler.isPathCorrect("/tasks");
        boolean condition2 = handler.isPathCorrect("/tasks/3");
        boolean condition3 = handler.isPathCorrect("/tasks/fsff");
        boolean condition4 = handler.isPathCorrect("/tasks/3/3");
        boolean condition5 = handler.isPathCorrect("/tasks/3/ds");

        boolean condition = condition1 && condition2 && !condition3 && !condition4 && !condition5;
        Assertions.assertTrue(condition);
    }

    public void initializeHandler() {
        TaskManager tasksManager = Managers.getDefaultFileBackedTaskManager();
        Gson gson = HttpTaskServer.getDefaultGson();
        handler = new TasksHandler(tasksManager, gson);
    }

    public static Task createDefaultTask() {
        return new Task((short) 1, "Task_1", "Description of Task_1", "NEW",
                LocalDateTime.parse("2024-11-11T13:00"), Duration.ofMinutes(33));
    }
}
