package lib.web.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import lib.managers.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {
    public HistoryHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    void handleGet(HttpExchange httpExchange, String path) throws IOException {
        String body = gson.toJson(taskManager.getHistoryManager().getHistory());
        sendText(httpExchange, OK_CODE, body);
    }

    @Override
    public boolean isPathCorrect(String path) {
        return path.equals("/history");
    }
}
