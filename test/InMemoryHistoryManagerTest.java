import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Task;

class InMemoryHistoryManagerTest {
    private static final InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

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

}