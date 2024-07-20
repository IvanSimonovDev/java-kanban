package lib.web.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import lib.exceptions.CollisionException;
import lib.exceptions.NotFoundException;
import lib.managers.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {
    public HistoryHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    void handleWhenPathCorrect(HttpExchange httpExchange, String path) throws IOException, NotFoundException, CollisionException {
        String method = httpExchange.getRequestMethod();

        switch (method) {
            case "GET":
                handleGet(httpExchange, path);
                break;
            default:
                sendText(httpExchange, INTERNAL_SERVER_ERROR_CODE, INTERNAL_SERVER_ERROR_NOTIFICATION);
        }
    }

    @Override
    void handleGet(HttpExchange httpExchange, String path) throws IOException {
        String body = gson.toJson(taskManager.getHistoryManager().getHistory());
        sendText(httpExchange, OK_CODE, body);
    }

    @Override
    void handleGetAllTasksOfType(HttpExchange httpExchange) throws IOException {

    }

    @Override
    void handleGetTaskOfTypeById(HttpExchange httpExchange, short id) throws NotFoundException, IOException {

    }

    @Override
    void handleCreateOrUpdateTaskOfType(HttpExchange httpExchange) throws IOException, CollisionException {

    }

    @Override
    void handleDeleteTaskOfTypeById(HttpExchange httpExchange, short id)  {

    }

    @Override
    public  boolean isPathCorrect(String path) {
        return path.equals("/history");
    }
}
