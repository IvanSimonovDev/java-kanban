import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.util.List;

class InMemoryTaskManagerTest {
    private static InMemoryTaskManager inMemoryTaskManager;

    @BeforeEach
    public void taskManagerCreation() {
        inMemoryTaskManager = new InMemoryTaskManager();
    }

    @Test
    public void canCreateSubTaskAndFindItById() {

        Epic epicOfSubTask = new Epic("Epic_1", "Description of Epic_1", "NEW");
        SubTask subTask = new SubTask("SubTask_1", "Description of SubTask_1", "NEW", epicOfSubTask.id);

        inMemoryTaskManager.createEpic(epicOfSubTask);
        inMemoryTaskManager.createSubTask(subTask);

        SubTask returnedSubTask = inMemoryTaskManager.getSubTask(subTask.id);

        Assertions.assertEquals(subTask, returnedSubTask);
    }

    @Test
    public void canCreateTaskAndFindItById() {

        Task task = new Task("Task_1", "Description of Task_1", "NEW");

        inMemoryTaskManager.createTask(task);

        Task returnedTask = inMemoryTaskManager.getTask(task.id);

        Assertions.assertEquals(task, returnedTask);
    }

    @Test
    public void canCreateEpicAndFindItById() {

        Epic epic = new Epic("Epic_1", "Description of Epic_1", "NEW");

        inMemoryTaskManager.createEpic(epic);

        Epic returnedEpic = inMemoryTaskManager.getEpic(epic.id);

        Assertions.assertEquals(epic, returnedEpic);
    }

    @Test
    public void TaskWithSetIdAndTaskWithGeneratedIdDoNotConflict() {
        Task task = new Task("Task_1", "Description of Task_1", "NEW");
        inMemoryTaskManager.createTask(task);

        Task updatedTask = new Task(task.id, "Another Task_1", "Description of Task_1", "DONE");
        inMemoryTaskManager.updateTask(updatedTask);

        List<Task> listOfTasks = inMemoryTaskManager.getTasksList();

        boolean condition = (listOfTasks.size() == 1) &&
                inMemoryTaskManager.getTask(updatedTask.id).title.equals("Another Task_1");

        Assertions.assertTrue(condition);
    }

    @Test
    public void TaskDoesNotChangeAfterAddingToManager() {
        Task task = new Task("Task_1", "Description of Task_1", "NEW");
        inMemoryTaskManager.createTask(task);
        Task returnedTask = inMemoryTaskManager.getTask(task.id);
        boolean condition = (task.id == returnedTask.id) && (task.title.equals(returnedTask.title)) &&
                (task.description.equals(returnedTask.description)) && (task.status.equals(returnedTask.status));

        Assertions.assertTrue(condition);
    }

    



}