package lib.web.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import lib.managers.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends HistoryHandler {
    public PrioritizedHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    void handleGet(HttpExchange httpExchange, String path) throws IOException {
        String body = gson.toJson(taskManager.getPrioritizedTasks());
        sendText(httpExchange, OK_CODE, body);
    }

    @Override
    public boolean isPathCorrect(String path) {
        return path.equals("/prioritized");
    }
}
