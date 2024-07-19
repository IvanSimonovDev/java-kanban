package lib.web.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import lib.exceptions.CollisionException;
import lib.exceptions.ManagerSaveLoadException;
import lib.exceptions.NotFoundException;
import lib.managers.TaskManager;
import lib.tasks.Task;

import java.io.IOException;
import java.net.URI;

public class TasksHandler extends BaseHttpHandler {
    public TasksHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            System.out.println("1");
            if (!isPathCorrect(path)) {
                System.out.println("notCorrectPath");
                throw new NotFoundException(WRONG_URL_PATH_NOTIFICATION);
            } else {
                handleWhenPathCorrect(httpExchange, path);
                System.out.println("3");
            }
        } catch (CollisionException exc) {
            sendHasInteractions(httpExchange);
            System.out.println("collision");
        } catch (NotFoundException exc) {
            sendNotFound(httpExchange);
            System.out.println("notFound");
        } catch (IOException | ManagerSaveLoadException exc) {
            sendText(httpExchange, INTERNAL_SERVER_ERROR_CODE, INTERNAL_SERVER_ERROR_NOTIFICATION);
            System.out.println("500");
        }
        System.out.println("end");
    }

    private void handleWhenPathCorrect(HttpExchange httpExchange, String path) throws IOException, NotFoundException, CollisionException {
        String method = httpExchange.getRequestMethod();
        short id;

        switch (method) {
            case "GET":
                if (pathContainsNumberOfParts(path, 1)) {
                    handleGetAllTasks(httpExchange);
                } else {
                    id = getIdFromPath(path);
                    handleGetTaskById(httpExchange, id);
                }
                break;
            case "POST":
                handleCreateOrUpdateTask(httpExchange);
                break;
            case "DELETE":
                id = getIdFromPath(path);
                handleDeleteTaskById(httpExchange, id);
                break;
            default:
                sendText(httpExchange, INTERNAL_SERVER_ERROR_CODE, INTERNAL_SERVER_ERROR_NOTIFICATION);
        }
    }

    private void handleGetAllTasks(HttpExchange httpExchange) throws IOException {
        String body = gson.toJson(taskManager.getTasksList());
        sendText(httpExchange, OK_CODE, body);

    }

    private void handleGetTaskById(HttpExchange httpExchange, short id) throws NotFoundException, IOException {
        String body = gson.toJson(taskManager.getTask(id));
        sendText(httpExchange, OK_CODE, body);
    }

    private void handleCreateOrUpdateTask(HttpExchange httpExchange) throws IOException, CollisionException {
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

    private void handleDeleteTaskById(HttpExchange httpExchange, short id) throws IOException {
        taskManager.deleteTask(id);
        sendWithoutBody(httpExchange, OK_CODE);
    }

    // Method checks that path has this structure: /.../{number}
    boolean isPathCorrect(String path) {
        String[] pathParts = path.split(PATH_DELIMITER);
        int pathPartsNumber = pathParts.length;
        int correctAmount1 = 2;
        int correctAmount2 = 3;

        boolean condition1 = pathPartsNumber == correctAmount1 || pathPartsNumber == correctAmount2;
        boolean condition2 = true;
        if (pathPartsNumber == correctAmount2) {
            try {
                int id = Integer.parseInt(pathParts[correctAmount2 - 1]);
            } catch (NumberFormatException exc) {
                condition2 = false;
            }
        }

        return condition1 && condition2;
    }

    boolean pathContainsNumberOfParts(String path, int numberOfParts) {
        String[] pathParts = path.split(PATH_DELIMITER);
        return pathParts.length - 1 == numberOfParts;
    }

    short getIdFromPath(String path) {
        String[] pathParts = path.split(PATH_DELIMITER);
        int idPosition = 2;
        try {
            return Short.parseShort(pathParts[idPosition]);
        } catch (NumberFormatException exc) {
            throw new NotFoundException("Requested task not found");
        }

    }

}
