package lib.web.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import lib.managers.TaskManager;

import java.io.IOException;

public class SubtasksHandler extends BaseHttpHandler {
    public SubtasksHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
