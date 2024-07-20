package lib.web.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import lib.exceptions.CollisionException;
import lib.exceptions.NotFoundException;
import lib.managers.TaskManager;
import lib.tasks.Task;

import java.io.IOException;

public class TasksHandler extends BaseHttpHandler {
    public TasksHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    void handleGetAllTasksOfType(HttpExchange httpExchange) throws IOException {
        String body = gson.toJson(taskManager.getTasksList());
        sendText(httpExchange, OK_CODE, body);

    }

    @Override
    void handleGetTaskOfTypeById(HttpExchange httpExchange, short id) throws NotFoundException, IOException {
        String body = gson.toJson(taskManager.getTask(id));
        sendText(httpExchange, OK_CODE, body);
    }

    @Override
    void handleCreateOrUpdateTaskOfType(HttpExchange httpExchange) throws IOException, CollisionException {
        String requestBody = readRequestBody(httpExchange);
        Task taskFromJson = gson.fromJson(requestBody, Task.class);

        if (taskFromJson.id == 0) {
            Task newTask = new Task(
                    taskFromJson.title,
                    taskFromJson.description,
                    taskFromJson.status.toString(),
                    taskFromJson.startTime,
                    taskFromJson.duration
            );
            taskManager.createTask(newTask);
        } else {
            Task updatedTask = new Task(
                    taskFromJson.id,
                    taskFromJson.title,
                    taskFromJson.description,
                    taskFromJson.status.toString(),
                    taskFromJson.startTime,
                    taskFromJson.duration
            );
            taskManager.updateTask(updatedTask);
        }
        sendWithoutBody(httpExchange, CREATED_CODE);
    }

    @Override
    public void handleDeleteTaskOfTypeById(HttpExchange httpExchange, short id) throws IOException {
        taskManager.deleteTask(id);
        sendWithoutBody(httpExchange, OK_CODE);
    }

    @Override
    public boolean isPathCorrect(String path) {
        String[] splitPath = path.split(PATH_DELIMITER);
        int splitPathSize = splitPath.length;
        int correctSplitPathSize1 = 2;
        int correctSplitPathSize2 = 3;

        boolean condition1 = splitPathSize == correctSplitPathSize1 || splitPathSize == correctSplitPathSize2;
        boolean condition2 = true;
        if (splitPathSize == correctSplitPathSize2) {
            try {
                int id = Integer.parseInt(splitPath[correctSplitPathSize2 - 1]);
            } catch (NumberFormatException exc) {
                condition2 = false;
            }
        }

        return condition1 && condition2;
    }
}
