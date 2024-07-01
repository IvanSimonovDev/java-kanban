import lib.HistoryManager;
import lib.InMemoryHistoryManager;
import lib.InMemoryTaskManager;
import lib.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import lib.tasks.Epic;
import lib.tasks.SubTask;
import lib.tasks.Task;

import java.util.ArrayList;
import java.util.List;

class InMemoryHistoryManagerTest {
    private static HistoryManager inMemoryHistoryManager;
    private static TaskManager inMemoryTaskManager;

    @BeforeEach
    public void createAndInitializeManagers() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
        inMemoryTaskManager = new InMemoryTaskManager();
    }

    @Test
    public void tasksSavePreviousVersionsAfterAddingToHistory() {
        short id = 44;
        Task task = new Task(id, "Task_1", "Description_1", "NEW");
        inMemoryHistoryManager.add(task);
        String previousTitle = task.title;
        task.title = "New Task_1 Title";

        boolean condition = inMemoryHistoryManager.getHistory().getFirst().title.equals(previousTitle);
        Assertions.assertTrue(condition);

    }

    @Test
    public void managerCanStoreMoreThanTenTasks() {
        int limit = 15;
        ArrayList<Task> tasks = new ArrayList<>();

        for (short i = 1; i <= limit; i++) {
            short id = i;
            String title = "Title" + id;
            String description = "Description" + id;
            String status = "NEW";
            Task task = new Task(id, title, description, status);
            inMemoryHistoryManager.add(task);
            tasks.add(task);
        }

        Assertions.assertTrue(inMemoryHistoryManager.getHistory().containsAll(tasks));
    }

    @Test
    public void managerCanDeleteTaskFromHistory() {
        short id = 3132;
        Task task = new Task(id, "Task_1", "Description_1", "NEW");

        inMemoryHistoryManager.add(task);
        boolean conditionFst = inMemoryHistoryManager.getHistory().contains(task);

        inMemoryHistoryManager.remove(task.id);
        boolean conditionSnd = inMemoryHistoryManager.getHistory().isEmpty();

        Assertions.assertTrue(conditionFst && conditionSnd);

    }

    @Test
    public void tasksAreDeletedFromHistoryWhenTheyAreDeletedFromTaskManager() {
        inMemoryHistoryManager = inMemoryTaskManager.getHistoryManager();

        List<Task> tasks = createAndGetAllTypesOfTasks();


        boolean conditionFst = inMemoryHistoryManager.getHistory().containsAll(tasks) &&
                (inMemoryHistoryManager.getHistory().size() == 3);

        int indexOfTask = 0;
        int indexOfEpic = 2;

        inMemoryTaskManager.deleteTask(tasks.get(indexOfTask).id);
        inMemoryTaskManager.deleteEpic(tasks.get(indexOfEpic).id);

        boolean conditionSnd = inMemoryHistoryManager.getHistory().isEmpty();

        Assertions.assertTrue(conditionFst && conditionSnd);
    }

    @Test
    public void tasksInHistoryInCorrectOrder() {
        inMemoryHistoryManager = inMemoryTaskManager.getHistoryManager();

        List<Task> tasks = createAndGetAllTypesOfTasks();


        List<Task> history = inMemoryHistoryManager.getHistory();

        Assertions.assertEquals(new ArrayList<Task>(tasks), history);


    }

    public List<Task> createAndGetAllTypesOfTasks() {
        short epicId = 100;
        Epic epic = new Epic(epicId, "Epic_1", "Description_1", "NEW");
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.getEpic(epic.id);

        short subtaskId = 101;
        SubTask subTask = new SubTask(subtaskId, "SubTask_1", "Description_2",
                "NEW", epicId);
        inMemoryTaskManager.createSubTask(subTask);
        inMemoryTaskManager.getSubTask(subTask.id);

        short taskId = 102;
        Task task = new Task(taskId, "Task_1", "Description_3", "NEW");
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.getTask(task.id);

        return List.of(task, subTask, epic);
    }
}