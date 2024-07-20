import com.google.gson.Gson;
import lib.managers.Managers;
import lib.managers.TaskManager;
import lib.web.handlers.BaseHttpHandler;
import lib.web.handlers.TasksHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TasksHandlerTest {
    static BaseHttpHandler handler;

    @BeforeEach
    public void initializeHandler() {
        TaskManager tasksManager = Managers.getDefaultFileBackedTaskManager();
        Gson gson = HttpTaskServer.getDefaultGson();
        handler = new TasksHandler(tasksManager, gson);
    }

    @Test
    public void pathCheckShouldWorkCorrect() {
        boolean condition1 = handler.isPathCorrect("/tasks");
        boolean condition2 = handler.isPathCorrect("/tasks/3");
        boolean condition3 = handler.isPathCorrect("/tasks/fsff");
        boolean condition4 = handler.isPathCorrect("/tasks/3/3");
        boolean condition5 = handler.isPathCorrect("/tasks/3/ds");

        boolean condition = condition1 && condition2 && !condition3 && !condition4 && !condition5;
        Assertions.assertTrue(condition);
    }
}
