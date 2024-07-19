package lib.managers;

import lib.exceptions.ManagerSaveLoadException;
import lib.tasks.Epic;
import lib.tasks.SubTask;
import lib.tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    private static final String FILE_DATA_FORMAT = "id,type,title,description,status,epicId,startTime,duration";
    private static final String DEFAULT_FILE_STORAGE_STRING_PATH = "resources/file_storage.csv";
    private final File storage;

    public FileBackedTaskManager() {
        super();
        this.storage = new File(DEFAULT_FILE_STORAGE_STRING_PATH);
        this.loadData();
    }

    public FileBackedTaskManager(File storage) {
        super();
        this.storage = storage;
        this.loadData();
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        return new FileBackedTaskManager(file);
    }

    public static String getDefaultFileStorageStringPath() {
        return DEFAULT_FILE_STORAGE_STRING_PATH;
    }

    public static String getFileDataFormat() {
        return FILE_DATA_FORMAT;
    }

    @Override
    public void createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
    }

    @Override
    public void deleteSubTask(Short id) {
        super.deleteSubTask(id);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void deleteTask(short id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void updateTask(Task updatedTask) {
        super.updateTask(updatedTask);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void deleteEpic(short id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
        super.updateEpic(updatedEpic);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }


    private void save() {
        try (BufferedWriter bufferedWriter =
                     new BufferedWriter(new FileWriter(storage, StandardCharsets.UTF_8))) {
            bufferedWriter.write(FILE_DATA_FORMAT + "\n");
            saveEveryTask(bufferedWriter);

        } catch (IOException e) {
            throw new ManagerSaveLoadException("500. Ошибка при сохранении данных в хранилище.");
        }
    }

    private void saveEveryTask(Writer writer) throws java.io.IOException {
        Map<Short, Task> tempStorage = new LinkedHashMap<>(taskStorage);
        tempStorage.putAll(epicStorage);
        tempStorage.putAll(subTaskStorage);

        for (Task task : tempStorage.values()) {
            String output = "";
            String stringStartTime = (task.startTime == null) ? "null" : task.startTime.toString();
            String stringDuration = (task.duration == null) ? "null" : String.valueOf(task.duration.toMinutes());

            switch (task.taskType) {
                case TASK:
                    output = String.join(",", String.valueOf(task.id), "Task",
                            task.title, task.description, task.status.toString(), "-",
                            stringStartTime, stringDuration, "\n");
                    break;
                case EPIC:
                    output = String.join(",", String.valueOf(task.id), "Epic",
                            task.title, task.description, task.status.toString(), "-",
                            "null", "null", "\n");
                    break;
                case SUBTASK:
                    SubTask subTask = (SubTask) task;
                    output = String.join(",", String.valueOf(subTask.id), "SubTask", subTask.title,
                            subTask.description, subTask.status.toString(), String.valueOf(subTask.epicId),
                            stringStartTime, stringDuration, "\n");
                    break;
            }

            writer.write(output);


        }
    }

    public void loadData() {
        try {

            if (!storage.exists()) {
                FileWriter fileWriter = new FileWriter(storage, StandardCharsets.UTF_8);

                fileWriter.write(FILE_DATA_FORMAT);
                fileWriter.close();
            } else {
                loadDataFromFile();
            }

        } catch (IOException e) {
            throw new ManagerSaveLoadException("500. Ошибка при получении данных из хранилища или его создании.");
        }
    }

    private void loadDataFromFile() throws IOException {
        try (BufferedReader bufferedReader =
                     new BufferedReader(new FileReader(storage, StandardCharsets.UTF_8))) {
            // Первую строку пропускаем - там задан формат.
            bufferedReader.readLine();
            while (bufferedReader.ready()) {
                processLineInFile(bufferedReader.readLine());
            }
        }

    }

    private void processLineInFile(String line) {
        int idPosition = 0;
        int typePosition = 1;
        int titlePosition = 2;
        int descriptionPosition = 3;
        int statusPosition = 4;
        int epicIdPosition = 5;
        int startTimePosition = 6;
        int durationPosition = 7;

        String[] lineParts = line.split(",");

        short id = Short.parseShort(lineParts[idPosition]);
        String title = lineParts[titlePosition];
        String description = lineParts[descriptionPosition];
        String status = lineParts[statusPosition];

        LocalDateTime startTime;
        if (!lineParts[startTimePosition].equals("null")) {
            startTime = LocalDateTime.parse(lineParts[startTimePosition]);
        } else {
            startTime = null;
        }

        Duration duration;
        if (!lineParts[durationPosition].equals("null")) {
            duration = Duration.ofMinutes(Integer.parseInt(lineParts[durationPosition]));
        } else {
            duration = null;
        }

        switch (lineParts[typePosition]) {
            case "SubTask":
                short epicId = Short.parseShort(lineParts[epicIdPosition]);
                SubTask subtask = new SubTask(id, title, description, status, epicId, startTime, duration);
                createSubTask(subtask);
                break;
            case "Task":
                Task task = new Task(id, title, description, status, startTime, duration);
                createTask(task);
                break;
            case "Epic":
                Epic epic = new Epic(id, title, description, status);
                createEpic(epic);
                break;
        }
    }
}
