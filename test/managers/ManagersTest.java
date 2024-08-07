package managers;

import lib.managers.InMemoryHistoryManager;
import lib.managers.InMemoryTaskManager;
import lib.managers.Managers;
import lib.tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class ManagersTest {


    @Test
    public void InMemoryTaskManagerCorrectlyInitialized() {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefaultInMemoryTaskManager();

        boolean condition = inMemoryTaskManager.getSubTasksList().isEmpty() &&
                inMemoryTaskManager.getTasksList().isEmpty() &&
                inMemoryTaskManager.getEpicsList().isEmpty() &&
                (inMemoryTaskManager.getHistoryManager().getClass() == InMemoryHistoryManager.class);

        Assertions.assertTrue(condition);
    }

    @Test
    public void InMemoryHistoryManagerCorrectlyInitialized() {
        InMemoryHistoryManager inMemoryHistoryManager = (InMemoryHistoryManager) Managers.getDefaultHistory();

        boolean condition = inMemoryHistoryManager.getHistory().isEmpty();

        Assertions.assertTrue(condition);
    }

    @Test
    public void InMemoryTaskManagerReadyForWork() {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefaultInMemoryTaskManager();

        short id = 1;
        Task taskFst = new Task(id, "Task_1", "Task_1 Description", "NEW");
        inMemoryTaskManager.createTask(taskFst);
        Task returnedTask = inMemoryTaskManager.getTask(taskFst.id);

        Assertions.assertEquals(taskFst, returnedTask);
    }

    @Test
    public void InMemoryHistoryManagerReadyForWork() {
        InMemoryHistoryManager inMemoryHistoryManager = (InMemoryHistoryManager) Managers.getDefaultHistory();
        short id = 1;
        Task taskFst = new Task(id, "Task_1", "Task_1 Description", "NEW");

        inMemoryHistoryManager.add(taskFst);

        Assertions.assertTrue(inMemoryHistoryManager.getHistory().contains(taskFst));
    }


}