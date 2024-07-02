import lib.FileBackedTaskManager;
import lib.exceptions.ManagerSaveLoadException;
import lib.tasks.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    private static File fileStorage;
    private static FileWriter fileWriter;

    private static File defaultStorage;

    @Override
    @BeforeEach
    public void taskManagerCreation() {
        defaultStorage = new File(FileBackedTaskManager.getDefaultFileStorageStringPath());
        defaultStorage.delete();
        fileStorage = null;
        taskManager = new FileBackedTaskManager();
    }

    @AfterEach
    public void deleteDefaultStorage() {
        defaultStorage.delete();
    }

    @Test
    public void shouldGetNoTasksFromEmptyFile() throws IOException {
        initializeEmptyFileStorageAndManager();

        boolean conditionFst = taskManager.getTasksList().isEmpty();
        boolean conditionSnd = taskManager.getSubTasksList().isEmpty();
        boolean conditionThd = taskManager.getEpicsList().isEmpty();

        Assertions.assertTrue(conditionFst && conditionSnd && conditionThd);
    }

    @Test
    public void shouldBeNoTasksInFileWhenManagerIsEmpty() throws IOException {
        initializeEmptyFileStorageAndManager();
        long emptyFileStorageSize = fileStorage.length();

        short taskId = 22;
        Task task = new Task(taskId, "Task_1", "Description of Task_1", "NEW");
        taskManager.createTask(task);
        taskManager.deleteTask(taskId);

        boolean conditionFst = taskManager.getTasksList().isEmpty();
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

        taskManager.createTask(taskFst);
        taskManager.createTask(taskSnd);
        taskManager.createTask(taskThd);

        FileBackedTaskManager secondManager = FileBackedTaskManager.loadFromFile(fileStorage);

        boolean condition = taskManager.getTasksList().equals(secondManager.getTasksList());

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
                .peek(task -> taskManager.createTask(task))
                .toList();
        List<Task> firstManagerTasks = taskManager.getTasksList();
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
        taskManager = FileBackedTaskManager.loadFromFile(fileStorage);
    }

    @Test
    public void shouldThrowExceptionWhenCanNotLoadFromPath() {
        Assertions.assertThrows(ManagerSaveLoadException.class,
                () -> {
                    fileStorage = new File("/not_existing_dir");
                    fileStorage.mkdir();
                    taskManager = FileBackedTaskManager.loadFromFile(fileStorage);
                }
        );
    }

    @Test
    public void shouldThrowExceptionWhenCanNotSaveToFile() {
        Assertions.assertThrows(ManagerSaveLoadException.class,
                () -> {
                    defaultStorage.delete();
                    defaultStorage.mkdir();
                    Task task = new Task((short) 1, "Task_1", "Description of Task_1", "NEW");
                    taskManager.createTask(task);
                }
        );
    }
}

