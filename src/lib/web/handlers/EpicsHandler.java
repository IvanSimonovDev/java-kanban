package lib.web.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import lib.exceptions.CollisionException;
import lib.exceptions.NotFoundException;
import lib.managers.TaskManager;
import lib.tasks.Epic;

import java.io.IOException;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler {
    public EpicsHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    void handleGet(HttpExchange httpExchange, String path) throws IOException, NotFoundException {
        if (pathContainsNumberOfParts(path, 1)) {
            handleGetAllTasksOfType(httpExchange);
        } else if (pathContainsNumberOfParts(path, 2)) {
            handleGetTaskOfTypeById(httpExchange, getIdFromPath(path));
        } else {
            handleGetAllSubtasksOfEpicById(httpExchange, getIdFromPath(path));
        }
    }

    @Override
    void handleGetAllTasksOfType(HttpExchange httpExchange) throws IOException {
        String body = gson.toJson(taskManager.getEpicsList());
        sendText(httpExchange, OK_CODE, body);

    }

    @Override
    void handleGetTaskOfTypeById(HttpExchange httpExchange, short id) throws NotFoundException, IOException {
        String body = gson.toJson(taskManager.getEpic(id));
        sendText(httpExchange, OK_CODE, body);
    }

    @Override
    void handleCreateOrUpdateTaskOfType(HttpExchange httpExchange) throws IOException, CollisionException {
        String requestBody = readRequestBody(httpExchange);
        Epic epicFromJson = gson.fromJson(requestBody, Epic.class);

        if (epicFromJson.id == 0) {
            Epic newEpic = new Epic(
                    epicFromJson.title,
                    epicFromJson.description,
                    "NEW"
            );
            taskManager.createEpic(newEpic);
        } else {
            Epic updatedEpic = new Epic(
                    epicFromJson.id,
                    epicFromJson.title,
                    epicFromJson.description,
                    "NEW"
            );
            taskManager.updateEpic(updatedEpic);
        }
        sendWithoutBody(httpExchange, CREATED_CODE);
    }

    @Override
    public void handleDeleteTaskOfTypeById(HttpExchange httpExchange, short id) throws IOException {
        taskManager.deleteEpic(id);
        sendWithoutBody(httpExchange, OK_CODE);
    }

    public void handleGetAllSubtasksOfEpicById(HttpExchange httpExchange, short epicId) throws IOException {
        String body = gson.toJson(taskManager.subTasksOfEpic(epicId));
        sendText(httpExchange, OK_CODE, body);
    }

    @Override
    public boolean isPathCorrect(String path) {
        String[] splitPath = path.split(PATH_DELIMITER);
        int splitPathSize = splitPath.length;
        int correctSplitPathSize1 = 2;
        int correctSplitPathSize2 = 3;
        int correctSplitPathSize3 = 4;
        List<Integer> correctSplitPathSizes =
                List.of(correctSplitPathSize1, correctSplitPathSize2, correctSplitPathSize3);

        boolean condition1 = correctSplitPathSizes.contains(splitPathSize);
        boolean condition2 = true;
        if (splitPathSize != correctSplitPathSize1) {
            try {
                int id = Integer.parseInt(splitPath[correctSplitPathSize2 - 1]);

                condition2 = (splitPathSize != correctSplitPathSize3) ||
                        splitPath[splitPathSize - 1].equals("subtasks");
            } catch (NumberFormatException exc) {
                condition2 = false;
            }
        }

        return condition1 && condition2;
    }

}
