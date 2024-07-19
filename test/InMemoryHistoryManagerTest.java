import lib.managers.HistoryManager;
import lib.managers.InMemoryHistoryManager;
import lib.managers.InMemoryTaskManager;
import lib.managers.TaskManager;
import lib.tasks.Epic;
import lib.tasks.SubTask;
import lib.tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class InMemoryHistoryManagerTest {
    private static final short TASK_ID = 1;
    private static Task task;
    private static SubTask subTask;
    private static Epic epic;
    private static HistoryManager inMemoryHistoryManager;
    private static TaskManager inMemoryTaskManager;

    @BeforeEach
    public void createAndInitializeManagers() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
        inMemoryTaskManager = new InMemoryTaskManager();
    }

    @BeforeEach
    public void setNullForTaskAndSubTaskAndEpic() {
        task = null;
        subTask = null;
        epic = null;
    }

    @Test
    public void tasksSavePreviousVersionsAfterAddingToHistory() {
        createTask(TASK_ID);
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
        createTask(TASK_ID);

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
        epic = new Epic(epicId, "Epic_1", "Description_1", "NEW");
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.getEpic(epic.id);

        short subtaskId = 101;
        subTask = new SubTask(subtaskId, "SubTask_1", "Description_2", "NEW", epicId);
        inMemoryTaskManager.createSubTask(subTask);
        inMemoryTaskManager.getSubTask(subTask.id);

        short taskId = 102;
        task = new Task(taskId, "Task_1", "Description_3", "NEW");
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.getTask(task.id);

        return List.of(task, subTask, epic);
    }

    @Test
    public void historyShouldBeEmptyAfterCreatingHistoryManager() {
        Assertions.assertTrue(inMemoryHistoryManager.getHistory().isEmpty());
    }

    @Test
    public void tasksInHistoryShouldBeUnique() {
        createTask(TASK_ID);
        int maxRepeats = 5;
        for (int counter = 1; counter <= maxRepeats; counter++) {
            inMemoryTaskManager.getTask(TASK_ID);
        }

        List<Task> history = inMemoryTaskManager.getHistoryManager().getHistory();
        boolean condition1 = history.contains(task);
        boolean condition2 = history.size() == 1;

        Assertions.assertTrue(condition1 && condition2);
    }

    @Test
    public void deletionFromBeginningOfHistoryWorkCorrect() {
        createAndGetAllTypesOfTasks();
        inMemoryTaskManager.deleteTask(task.id);

        List<Task> history = inMemoryTaskManager.getHistoryManager().getHistory();
        boolean condition1 = history.size() == 2;
        boolean condition2 = history.get(0).equals(subTask);
        boolean condition3 = history.get(1).equals(epic);

        Assertions.assertTrue(condition1 && condition2 && condition3);
    }

    @Test
    public void deletionFromMiddleOfHistoryWorkCorrect() {
        createAndGetAllTypesOfTasks();
        inMemoryTaskManager.deleteSubTask(subTask.id);

        List<Task> history = inMemoryTaskManager.getHistoryManager().getHistory();
        boolean condition1 = history.size() == 2;
        boolean condition2 = history.get(0).equals(task);
        boolean condition3 = history.get(1).equals(epic);

        Assertions.assertTrue(condition1 && condition2 && condition3);
    }

    @Test
    public void deletionFromEndOfHistoryWorkCorrect() {
        createAndGetAllTypesOfTasks();
        inMemoryTaskManager.getEpic(epic.id);
        inMemoryTaskManager.getSubTask(subTask.id);
        inMemoryTaskManager.deleteTask(task.id);

        List<Task> history = inMemoryTaskManager.getHistoryManager().getHistory();
        boolean condition1 = history.size() == 2;
        boolean condition2 = history.get(0).equals(subTask);
        boolean condition3 = history.get(1).equals(epic);

        Assertions.assertTrue(condition1 && condition2 && condition3);
    }

    public void createTask(short taskId) {
        task = new Task(taskId, "Task1", "Description1", "NEW");
        inMemoryTaskManager.createTask(task);
    }
}