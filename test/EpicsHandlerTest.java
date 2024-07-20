import com.google.gson.Gson;
import lib.managers.Managers;
import lib.managers.TaskManager;
import lib.web.handlers.BaseHttpHandler;
import lib.web.handlers.EpicsHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class EpicsHandlerTest {
    BaseHttpHandler handler;

    @BeforeEach
    public void initializeHandler() {
        TaskManager tasksManager = Managers.getDefaultFileBackedTaskManager();
        Gson gson = HttpTaskServer.getDefaultGson();
        handler = new EpicsHandler(tasksManager, gson);
    }

    @Test
    public void pathCheckShouldWorkCorrect() {
        boolean condition1 = handler.isPathCorrect("/epics");
        boolean condition2 = handler.isPathCorrect("/epics/3");
        boolean condition3 = handler.isPathCorrect("/epics/fsff");
        boolean condition4 = handler.isPathCorrect("/epics/3/subtasks");
        boolean condition5 = handler.isPathCorrect("/epics/3/ds");
        boolean condition6 = handler.isPathCorrect("/epics/3/3");
        boolean condition7 = handler.isPathCorrect("/epics/fsff/subtasks");
        boolean condition8 = handler.isPathCorrect("/epics/fsff/ds");
        boolean condition9 = handler.isPathCorrect("/epics/fsff/3");
        boolean condition10 = handler.isPathCorrect("/epics/3/subtasks/4");


        boolean condition = condition1 &&
                condition2 &&
                !condition3 &&
                condition4 &&
                !condition5 &&
                !condition6 &&
                !condition7 &&
                !condition8 &&
                !condition9 &&
                !condition10;

        Assertions.assertTrue(condition);
    }

}
