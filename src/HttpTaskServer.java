import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import lib.managers.Managers;
import lib.managers.TaskManager;
import lib.web.handlers.*;
import lib.web.typeAdapters.DurationAdapter;
import lib.web.typeAdapters.LocalDateTimeAdapter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int NET_PORT = 8080;
    private static final int BACKLOG_SETTING = 0;

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefaultFileBackedTaskManager();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                               .registerTypeAdapter(Duration.class, new DurationAdapter())
                               .setPrettyPrinting()
                               .create();

        HttpServer httpServer = HttpServer.create(new InetSocketAddress(NET_PORT), BACKLOG_SETTING);
        httpServer.createContext("/tasks", new TasksHandler(taskManager, gson));
       // httpServer.createContext("/subtasks", new SubtasksHandler(taskManager, gson));
       // httpServer.createContext("/epics", new EpicsHandler(taskManager, gson));
       // httpServer.createContext("/history", new HistoryHandler(taskManager, gson));
        //httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager, gson));
        httpServer.start();
    }
}
