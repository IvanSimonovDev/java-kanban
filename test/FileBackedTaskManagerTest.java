import lib.FileBackedTaskManager;
import lib.tasks.Epic;
import lib.tasks.Statuses;
import lib.tasks.SubTask;
import lib.tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class FileBackedTaskManagerTest {
    private static FileBackedTaskManager fileBackedTaskManager;
    private static File fileStorage;
    private static FileWriter fileWriter;

    @BeforeEach
    public void taskManagerCreation()  {
        File defaultStorage = new File(FileBackedTaskManager.getDefaultFileStorageStringPath());
        defaultStorage.delete();
        fileBackedTaskManager = new FileBackedTaskManager();
    }

    @Test
    public void canCreateSubTaskAndFindItById() {

        Epic epicOfSubTask = new Epic("Epic_1", "Description of Epic_1", "NEW");
        SubTask subTask = new SubTask("SubTask_1", "Description of SubTask_1", "NEW", epicOfSubTask.id);

        fileBackedTaskManager.createEpic(epicOfSubTask);
        fileBackedTaskManager.createSubTask(subTask);

        SubTask returnedSubTask = fileBackedTaskManager.getSubTask(subTask.id);

        Assertions.assertEquals(subTask, returnedSubTask);
    }

    @Test
    public void canCreateTaskAndFindItById() {

        Task task = new Task("Task_1", "Description of Task_1", "NEW");

        fileBackedTaskManager.createTask(task);

        Task returnedTask = fileBackedTaskManager.getTask(task.id);

        Assertions.assertEquals(task, returnedTask);
    }

    @Test
    public void canCreateEpicAndFindItById() {

        Epic epic = new Epic("Epic_1", "Description of Epic_1", "NEW");

        fileBackedTaskManager.createEpic(epic);

        Epic returnedEpic = fileBackedTaskManager.getEpic(epic.id);

        Assertions.assertEquals(epic, returnedEpic);
    }

    @Test
    public void TaskWithSetIdAndTaskWithGeneratedIdDoNotConflict() {
        Task task = new Task("Task_1", "Description of Task_1", "NEW");
        fileBackedTaskManager.createTask(task);

        Task updatedTask = new Task(task.id, "Another Task_1", "Description of Task_1", "DONE");
        fileBackedTaskManager.updateTask(updatedTask);

        List<Task> listOfTasks = fileBackedTaskManager.getTasksList();

        boolean condition = (listOfTasks.size() == 1) &&
                fileBackedTaskManager.getTask(updatedTask.id).title.equals("Another Task_1");

        Assertions.assertTrue(condition);
    }

    @Test
    public void TaskDoesNotChangeAfterAddingToManager() {
        Task task = new Task("Task_1", "Description of Task_1", "NEW");
        fileBackedTaskManager.createTask(task);
        Task returnedTask = fileBackedTaskManager.getTask(task.id);
        boolean condition = (task.id == returnedTask.id) && (task.title.equals(returnedTask.title)) &&
                (task.description.equals(returnedTask.description)) && (task.status.equals(returnedTask.status));

        Assertions.assertTrue(condition);
    }

    @Test
    public void epicShouldNotContainDeletedSubTask() {

        Epic epicOfSubTask = new Epic("Epic_1", "Description of Epic_1", "NEW");
        SubTask subTask = new SubTask("SubTask_1", "Description of SubTask_1", "NEW", epicOfSubTask.id);

        fileBackedTaskManager.createEpic(epicOfSubTask);
        fileBackedTaskManager.createSubTask(subTask);
        fileBackedTaskManager.deleteSubTask(subTask.id);

        Assertions.assertTrue(epicOfSubTask.subtasksIds.isEmpty());

    }

    @Test
    public void epicFieldsCanChangeOutsideOfTaskManager() {
        short epicId = 22;
        Epic epic = new Epic(epicId, "Epic_1", "Description of Epic_1", "NEW");

        short newEpicId = 11;
        String newEpicTitle = "Epic_2";
        String newEpicDescription = "Description of Epic_2";
        Statuses newEpicStatus = Statuses.IN_PROGRESS;
        ArrayList<Short> newSubtasksIds = new ArrayList<>(List.of(epicId));

        epic.id = newEpicId;
        epic.title = newEpicTitle;
        epic.description = newEpicDescription;
        epic.status = newEpicStatus;
        epic.subtasksIds = newSubtasksIds;

        boolean conditionFst = (epic.id == newEpicId);
        boolean conditionSnd = (epic.title.equals(newEpicTitle));
        boolean conditionThd = (epic.description.equals(newEpicDescription));
        boolean conditionFth = (epic.status == newEpicStatus);
        boolean conditionFifth = (epic.subtasksIds.equals(newSubtasksIds));

        Assertions.assertTrue(conditionFst && conditionSnd && conditionThd && conditionFth && conditionFifth);

    }

    @Test
    public void subTaskFieldsCanChangeOutsideOfTaskManager() {
        short subTaskId = 22;
        short epicId = 33;
        SubTask subTask = new SubTask(subTaskId, "SubTask_1", "Description of SubTask_1",
                "NEW", epicId);

        short newSubTaskId = 11;
        String newSubTaskTitle = "SubTask_2";
        String newSubTaskDescription = "Description of SubTask_2";
        Statuses newSubTaskStatus = Statuses.IN_PROGRESS;
        short newEpicId = 44;

        subTask.id = newSubTaskId;
        subTask.title = newSubTaskTitle;
        subTask.description = newSubTaskDescription;
        subTask.status = newSubTaskStatus;
        subTask.epicId = newEpicId;

        boolean conditionFst = (subTask.id == newSubTaskId);
        boolean conditionSnd = (subTask.title.equals(newSubTaskTitle));
        boolean conditionThd = (subTask.description.equals(newSubTaskDescription));
        boolean conditionFth = (subTask.status == newSubTaskStatus);
        boolean conditionFifth = (subTask.epicId == newEpicId);

        Assertions.assertTrue(conditionFst && conditionSnd && conditionThd && conditionFth && conditionFifth);

    }

    @Test
    public void taskFieldsCanChangeOutsideOfTaskManager() {
        short taskId = 22;
        Task task = new Task(taskId, "Task_1", "Description of Task_1",
                "NEW");

        short newTaskId = 11;
        String newTaskTitle = "Task_2";
        String newTaskDescription = "Description of Task_2";
        Statuses newTaskStatus = Statuses.IN_PROGRESS;

        task.id = newTaskId;
        task.title = newTaskTitle;
        task.description = newTaskDescription;
        task.status = newTaskStatus;

        boolean conditionFst = (task.id == newTaskId);
        boolean conditionSnd = (task.title.equals(newTaskTitle));
        boolean conditionThd = (task.description.equals(newTaskDescription));
        boolean conditionFth = (task.status == newTaskStatus);

        Assertions.assertTrue(conditionFst && conditionSnd && conditionThd && conditionFth);

    }

    @Test
    public void shouldGetNoTasksFromEmptyFile() throws IOException {

        initializeEmptyFileStorageAndManager();


        boolean conditionFst = fileBackedTaskManager.getTasksList().isEmpty();
        boolean conditionSnd = fileBackedTaskManager.getSubTasksList().isEmpty();
        boolean conditionThd = fileBackedTaskManager.getEpicsList().isEmpty();

        Assertions.assertTrue(conditionFst && conditionSnd && conditionThd);


    }

    @Test
    public void shouldBeNoTasksInFileWhenManagerIsEmpty() throws IOException {
        initializeEmptyFileStorageAndManager();
        long emptyFileStorageSize = fileStorage.length();

        short taskId = 22;
        Task task = new Task(taskId, "Task_1", "Description of Task_1", "NEW");
        fileBackedTaskManager.createTask(task);
        fileBackedTaskManager.deleteTask(taskId);

        boolean conditionFst = fileBackedTaskManager.getTasksList().isEmpty();
        boolean conditionSnd = (fileStorage.length() == emptyFileStorageSize);

        Assertions.assertTrue(conditionFst && conditionSnd);


    }

    @Test
    public void taskManagerShouldSaveAndLoadMoreThanOneTask() throws IOException {
        initializeEmptyFileStorageAndManager();

        short taskFstId = 22;
        short taskSndId = 23;
        short taskThdId = 24;

        Task taskFst = new Task(taskFstId, "Task_1", "Description of Task_1", "NEW");
        Task taskSnd = new Task(taskSndId, "Task_2", "Description of Task_2", "NEW");
        Task taskThd = new Task(taskThdId, "Task_3", "Description of Task_3", "NEW");

        fileBackedTaskManager.createTask(taskFst);
        fileBackedTaskManager.createTask(taskSnd);
        fileBackedTaskManager.createTask(taskThd);

        FileBackedTaskManager secondManager = FileBackedTaskManager.loadFromFile(fileStorage);

        boolean condition = fileBackedTaskManager.getTasksList().equals(secondManager.getTasksList());


        Assertions.assertTrue(condition);


    }

    @Test
    public void temporalFieldsShouldSaveToFileAndLoadFromFile() throws IOException {
        initializeEmptyFileStorageAndManager();
        Task taskFst = new Task((short) 1, "Task_1", "Description of Task_1", "NEW",
                LocalDateTime.parse("2024-11-11T13:00"), Duration.ofMinutes(33));
        Task taskSnd = new Task((short) 2, "Task_2", "Description of Task_2", "NEW",
                LocalDateTime.parse("2024-11-11T14:00"), Duration.ofMinutes(43));
        Task taskThd = new Task((short) 3, "Task_3", "Description of Task_3", "NEW",
                LocalDateTime.parse("2024-11-11T15:00"), Duration.ofMinutes(53));

        List.of(taskFst, taskSnd, taskThd)
                .stream()
                .peek(task -> fileBackedTaskManager.createTask(task))
                .toList();
        List<Task> firstManagerTasks = fileBackedTaskManager.getTasksList();
        FileBackedTaskManager secondManager = FileBackedTaskManager.loadFromFile(fileStorage);

        boolean condition1 = firstManagerTasks
                .stream()
                .filter(task -> !task.duration.equals(secondManager.getTask(task.id).duration))
                .toList()
                .isEmpty();

        boolean condition2 = firstManagerTasks
                .stream()
                .filter(task -> !task.startTime.equals(secondManager.getTask(task.id).startTime))
                .toList()
                .isEmpty();

        Assertions.assertTrue(condition1 && condition2);
    }


    private void initializeEmptyFileStorageAndManager() throws IOException {
        fileStorage = File.createTempFile("test_storage", ".csv");
        fileWriter = new FileWriter(fileStorage, StandardCharsets.UTF_8, true);
        fileWriter.write(FileBackedTaskManager.getFileDataFormat() + "\n");
        fileWriter.close();
        fileBackedTaskManager = FileBackedTaskManager.loadFromFile(fileStorage);
    }


}

