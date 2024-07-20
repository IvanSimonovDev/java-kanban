package lib.web.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lib.exceptions.CollisionException;
import lib.exceptions.ManagerSaveLoadException;
import lib.exceptions.NotFoundException;
import lib.managers.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements HttpHandler {
    public TaskManager taskManager;
    public Gson gson;
    public static final String INTERNAL_SERVER_ERROR_NOTIFICATION = "Internal Server Error.";
    public static final String WRONG_URL_PATH_NOTIFICATION = "Wrong path in URL";
    public static final int INTERNAL_SERVER_ERROR_CODE = 500;
    public static final int NOT_FOUND_ERROR_CODE = 404;
    public static final int NOT_ACCEPTABLE_ERROR_CODE = 406;
    public static final int OK_CODE = 200;
    public static final int CREATED_CODE = 201;
    public static final long BASIC_RESPONSE_BODY_SETTING = 0;
    public static final String PATH_DELIMITER = "/";
    public static final String CONTENT_TYPE = "application/json; charset=utf-8";

    public static final Charset encoding = StandardCharsets.UTF_8;

    public BaseHttpHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            if (!isPathCorrect(path)) {
                throw new NotFoundException(WRONG_URL_PATH_NOTIFICATION);
            } else {
                handleWhenPathCorrect(httpExchange, path);
            }
        } catch (CollisionException exc) {
            sendHasInteractions(httpExchange);
        } catch (NotFoundException exc) {
            sendNotFound(httpExchange);
        } catch (IOException | ManagerSaveLoadException exc) {
            sendText(httpExchange, INTERNAL_SERVER_ERROR_CODE, INTERNAL_SERVER_ERROR_NOTIFICATION);
        }
    }

    void handleWhenPathCorrect(HttpExchange httpExchange, String path) throws IOException, NotFoundException, CollisionException {
        String method = httpExchange.getRequestMethod();
        short id;

        switch (method) {
            case "GET":
                handleGet(httpExchange, path);
                break;
            case "POST":
                handleCreateOrUpdateTaskOfType(httpExchange);
                break;
            case "DELETE":
                id = getIdFromPath(path);
                handleDeleteTaskOfTypeById(httpExchange, id);
                break;
            default:
                sendText(httpExchange, INTERNAL_SERVER_ERROR_CODE, INTERNAL_SERVER_ERROR_NOTIFICATION);
        }
    }

    void handleGet(HttpExchange httpExchange, String path) throws IOException, NotFoundException {
        short id;
        if (pathContainsNumberOfParts(path, 1)) {
            handleGetAllTasksOfType(httpExchange);
        } else {
            id = getIdFromPath(path);
            handleGetTaskOfTypeById(httpExchange, id);
        }
    }

    abstract void handleGetAllTasksOfType(HttpExchange httpExchange) throws IOException;

    abstract void handleGetTaskOfTypeById(HttpExchange httpExchange, short id) throws NotFoundException, IOException;

    abstract void handleCreateOrUpdateTaskOfType(HttpExchange httpExchange) throws IOException, CollisionException;

    abstract void handleDeleteTaskOfTypeById(HttpExchange httpExchange, short id) throws IOException;

    public abstract boolean isPathCorrect(String path);

    boolean pathContainsNumberOfParts(String path, int numberOfParts) {
        String[] splitPath = path.split(PATH_DELIMITER);
        return splitPath.length - 1 == numberOfParts;
    }

    public short getIdFromPath(String path) {
        String[] splitPath = path.split(PATH_DELIMITER);
        int idPosition = 2;
        try {
            return Short.parseShort(splitPath[idPosition]);
        } catch (NumberFormatException exc) {
            throw new NotFoundException("Requested task not found");
        }
    }


    void sendText(HttpExchange httpExchange, int statusCode, String text) throws IOException {
        httpExchange.getResponseHeaders().set("Content-Type", CONTENT_TYPE);
        httpExchange.sendResponseHeaders(statusCode, BASIC_RESPONSE_BODY_SETTING);

        try (OutputStream bodyStream = httpExchange.getResponseBody()) {
            byte[] strInBytes = text.getBytes(StandardCharsets.UTF_8);
            bodyStream.write(strInBytes);
        }
    }

    void sendWithoutBody(HttpExchange httpExchange, int statusCode) throws IOException {
        sendText(httpExchange, statusCode, "");
    }

    private void sendNotFound(HttpExchange httpExchange) throws IOException {
        sendText(httpExchange, NOT_FOUND_ERROR_CODE, "");
    }

    private void sendHasInteractions(HttpExchange httpExchange) throws IOException {
        sendText(httpExchange, NOT_ACCEPTABLE_ERROR_CODE, "");
    }

    String readRequestBody(HttpExchange httpExchange) throws IOException {
        try (InputStream stream = httpExchange.getRequestBody()) {
            byte[] bytes = stream.readAllBytes();
            return new String(bytes, encoding);
        }
    }

}
