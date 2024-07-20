package lib.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import lib.managers.Managers;
import lib.managers.TaskManager;
import lib.web.gson.typeAdapters.DurationAdapter;
import lib.web.gson.typeAdapters.LocalDateTimeAdapter;
import lib.web.handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int NET_PORT = 8080;
    private static final int BACKLOG_SETTING = 0;

    HttpServer httpServer;
    TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        taskManager = Managers.getDefaultFileBackedTaskManager();
        Gson gson = getDefaultGson();

        httpServer = HttpServer.create(new InetSocketAddress(NET_PORT), BACKLOG_SETTING);
        httpServer.createContext("/tasks", new TasksHandler(taskManager, gson));
        httpServer.createContext("/subtasks", new SubtasksHandler(taskManager, gson));
        httpServer.createContext("/epics", new EpicsHandler(taskManager, gson));
        httpServer.createContext("/history", new HistoryHandler(taskManager, gson));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager, gson));
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
    }

    public void start() {
        httpServer.start();
    }

    public void deleteAllTasksOfAllTypes() {
        taskManager.deleteAllEpics();
        taskManager.deleteAllTasks();
    }

    public void stop() {
        int delay = 0;
        httpServer.stop(delay);
    }


    public static Gson getDefaultGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .setPrettyPrinting()
                .create();
    }
}
