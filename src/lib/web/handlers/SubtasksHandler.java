package lib.web.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import lib.exceptions.CollisionException;
import lib.exceptions.NotFoundException;
import lib.managers.TaskManager;
import lib.tasks.SubTask;

import java.io.IOException;

public class SubtasksHandler extends BaseHttpHandler {
    public SubtasksHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    void handleGetAll(HttpExchange httpExchange) throws IOException {
        String body = gson.toJson(taskManager.getSubTasksList());
        sendText(httpExchange, OK_CODE, body);

    }

    @Override
    void handleGetOne(HttpExchange httpExchange, short id) throws NotFoundException, IOException {
        String body = gson.toJson(taskManager.getSubTask(id));
        sendText(httpExchange, OK_CODE, body);
    }

    @Override
    void handlePost(HttpExchange httpExchange) throws IOException, CollisionException {
        String requestBody = readRequestBody(httpExchange);
        SubTask subTaskFromJson = gson.fromJson(requestBody, SubTask.class);
        taskManager.getEpic(subTaskFromJson.epicId);

        if (subTaskFromJson.id == 0) {
            SubTask newSubTask = new SubTask(
                    subTaskFromJson.title,
                    subTaskFromJson.description,
                    subTaskFromJson.status.toString(),
                    subTaskFromJson.epicId,
                    subTaskFromJson.startTime,
                    subTaskFromJson.duration
            );
            taskManager.createSubTask(newSubTask);
        } else {
            SubTask updatedSubTask = new SubTask(
                    subTaskFromJson.id,
                    subTaskFromJson.title,
                    subTaskFromJson.description,
                    subTaskFromJson.status.toString(),
                    subTaskFromJson.epicId,
                    subTaskFromJson.startTime,
                    subTaskFromJson.duration
            );
            taskManager.updateSubTask(updatedSubTask);
        }
        sendWithoutBody(httpExchange, CREATED_CODE);
    }

    @Override
    public void handleDelete(HttpExchange httpExchange, short id) throws IOException {
        taskManager.deleteSubTask(id);
        sendWithoutBody(httpExchange, OK_CODE);
    }

    @Override
    public boolean isPathCorrect(String path) {
        return (new TasksHandler(taskManager, gson)).isPathCorrect(path);
    }

}