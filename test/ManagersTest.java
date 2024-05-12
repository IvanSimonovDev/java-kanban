import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Task;


class ManagersTest {


    @Test
    public void InMemoryTaskManagerCorrectlyInitialized() {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();

        boolean condition = inMemoryTaskManager.subTaskStorage.isEmpty() && inMemoryTaskManager.taskStorage.isEmpty() &&
                inMemoryTaskManager.epicStorage.isEmpty() && (inMemoryTaskManager.getHistoryManager().getClass() ==
                InMemoryHistoryManager.class);

        Assertions.assertTrue(condition);
    }

    @Test
    public void InMemoryHistoryManagerCorrectlyInitialized() {
        InMemoryHistoryManager inMemoryHistoryManager = (InMemoryHistoryManager) Managers.getDefaultHistory();

        boolean condition = inMemoryHistoryManager.historyStorage.isEmpty();

        Assertions.assertTrue(condition);
    }

    @Test
    public void InMemoryTaskManagerReadyForWork() {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();

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

        Assertions.assertTrue(inMemoryHistoryManager.historyStorage.contains(taskFst));
    }



}