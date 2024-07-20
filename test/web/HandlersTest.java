package web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lib.tasks.Epic;
import lib.tasks.SubTask;
import lib.tasks.Task;
import lib.web.HttpTaskServer;
import lib.web.gson.typeAdapters.DurationAdapter;
import lib.web.gson.typeAdapters.LocalDateTimeAdapter;
import lib.web.gson.typeTokens.EpicsListTypeToken;
import lib.web.gson.typeTokens.SubTasksListTypeToken;
import lib.web.gson.typeTokens.TasksArrayTypeToken;
import lib.web.gson.typeTokens.TasksListTypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class HandlersTest {
    static final Charset UTF_8 = StandardCharsets.UTF_8;
    static final String URL_WITHOUT_PATH = "http://localhost:8080";
    static final String ACCEPT_HEADER_VALUE = "application/json";
    static HttpTaskServer httpTaskServer;
    static HttpClient httpClient;
    static HttpRequest httpRequest;
    static HttpRequest.Builder requestBuilder;
    static HttpRequest.BodyPublisher bodyPublisher;
    static HttpResponse<String> httpResponse;
    static HttpResponse.BodyHandler<String> bodyHandler;
    static Gson gson;

    @BeforeAll
    public static void createDefaultClientAndSetUpRequestBuilder() {
        httpClient = HttpClient.newHttpClient();
        bodyHandler = HttpResponse.BodyHandlers.ofString();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

    @BeforeEach
    public void createServerAndStartIt() throws IOException {
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        requestBuilder = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1).header("Accept", ACCEPT_HEADER_VALUE);
    }

    @AfterEach
    public void stopServer() {
        httpTaskServer.deleteAllTasksOfAllTypes();
        httpTaskServer.stop();
        httpResponse = null;
    }

    public HttpResponse<String> sendRequest(String method, String path, String body) throws IOException, InterruptedException {
        URI uri = URI.create(URL_WITHOUT_PATH + path);
        bodyPublisher = HttpRequest.BodyPublishers.ofString(body, UTF_8);
        httpRequest = requestBuilder
                .uri(uri)
                .method(method, bodyPublisher)
                .build();
        return httpClient.send(httpRequest, bodyHandler);
    }

    // Tasks
    public void requestToCreateOneTask(Task task) throws IOException, InterruptedException {
        String requestBody = convertTaskOfTypeToJsonWithoutId(task);
        httpResponse = sendRequest("POST", "/tasks", requestBody);
    }

    public Task requestForOneTask(short id) throws IOException, InterruptedException {
        httpResponse = sendRequest("GET", "/tasks/" + id, "");
        return parseJsonObject(httpResponse.body(), Task.class);
    }

    public void requestToDeleteOneTask(short id) throws IOException, InterruptedException {
        httpResponse = sendRequest("DELETE", "/tasks/" + id, "");
    }

    public List<Task> requestForAllTasks() throws IOException, InterruptedException {
        httpResponse = sendRequest("GET", "/tasks", "");
        return parseJsonArray(httpResponse.body(), new TasksListTypeToken());
    }

    // Subtasks
    public void requestToCreateOneSubTask(SubTask subTask) throws IOException, InterruptedException {
        String requestBody = convertTaskOfTypeToJsonWithoutId(subTask);
        httpResponse = sendRequest("POST", "/subtasks", requestBody);
    }

    public SubTask requestForOneSubTask(short id) throws IOException, InterruptedException {
        httpResponse = sendRequest("GET", "/subtasks/" + id, "");
        return parseJsonObject(httpResponse.body(), SubTask.class);
    }

    public void requestToDeleteOneSubTask(short id) throws IOException, InterruptedException {
        httpResponse = sendRequest("DELETE", "/subtasks/" + id, "");
    }

    public List<SubTask> requestForAllSubTasks() throws IOException, InterruptedException {
        httpResponse = sendRequest("GET", "/subtasks", "");
        return parseJsonArray(httpResponse.body(), new SubTasksListTypeToken());
    }

    // Epics
    public void requestToCreateOneEpic(Epic epic) throws IOException, InterruptedException {
        String requestBody = convertTaskOfTypeToJsonWithoutId(epic);
        httpResponse = sendRequest("POST", "/epics", requestBody);
    }

    public Epic requestForOneEpic(short id) throws IOException, InterruptedException {
        httpResponse = sendRequest("GET", "/epics/" + id, "");
        return parseJsonObject(httpResponse.body(), Epic.class);
    }

    public void requestToDeleteOneEpic(short id) throws IOException, InterruptedException {
        httpResponse = sendRequest("DELETE", "/epics/" + id, "");
    }

    public List<Epic> requestForAllEpics() throws IOException, InterruptedException {
        httpResponse = sendRequest("GET", "/epics", "");
        return parseJsonArray(httpResponse.body(), new EpicsListTypeToken());
    }

    public List<SubTask> requestForAllSubTasksOfEpic(short epicId) throws IOException, InterruptedException {
        String path = "/epics/" + epicId + "/subtasks";
        httpResponse = sendRequest("GET", path, "");
        return parseJsonArray(httpResponse.body(), new SubTasksListTypeToken());
    }

    public Task[] requestForHistory() throws IOException, InterruptedException {
        httpResponse = sendRequest("GET", "/history", "");
        return parseJsonArray(httpResponse.body(), new TasksArrayTypeToken());
    }

    public Task[] requestForPrioritizedTasksAndSubtasks() throws IOException, InterruptedException {
        httpResponse = sendRequest("GET", "/prioritized", "");
        return parseJsonArray(httpResponse.body(), new TasksArrayTypeToken());
    }

    public <T> T parseJsonArray(String jsonArr, TypeToken<T> typeToken) {
        return gson.fromJson(jsonArr, typeToken.getType());
    }

    public <T> T parseJsonObject(String jsonObj, Type type) {
        return gson.fromJson(jsonObj, type);
    }

    public <T> String convertTaskOfTypeToJsonWithoutId(T task) {
        JsonObject taskInJson = gson.toJsonTree(task).getAsJsonObject();
        taskInJson.remove("id");
        return taskInJson.toString();
    }

}
