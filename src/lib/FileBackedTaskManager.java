package lib;

import lib.exceptions.ManagerSaveException;
import lib.tasks.Epic;
import lib.tasks.SubTask;
import lib.tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    private static final String FILE_DATA_FORMAT = "id,type,title,description,status,epicId";
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
            throw new ManagerSaveException("500. Ошибка при считывании данных из хранилища.");
        }


    }

    private void saveEveryTask(Writer writer) throws java.io.IOException {
        Map<Short, Task> tempStorage = new LinkedHashMap<>(taskStorage);
        tempStorage.putAll(epicStorage);
        tempStorage.putAll(subTaskStorage);

        for (Task task : tempStorage.values()) {
            String canonicalName = task.getClass().getCanonicalName();
            String output = "";

            switch (canonicalName) {
                case "lib.tasks.Task":
                    output = String.join(",", String.valueOf(task.id), "Task",
                            task.title, task.description, task.status.toString(), "-", "\n");
                    break;
                case "lib.tasks.Epic":
                    output = String.join(",", String.valueOf(task.id), "Epic",
                            task.title, task.description, task.status.toString(), "-", "\n");
                    break;
                case "lib.tasks.SubTask":
                    SubTask subTask = (SubTask) task;
                    output = String.join(",", String.valueOf(subTask.id), "SubTask", subTask.title,
                            subTask.description, subTask.status.toString(), String.valueOf(subTask.epicId), "\n");
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
            int errorCode = 2;
            System.out.println("500. Ошибка при получении данных из хранилища или его создании.");
            System.exit(errorCode);
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

        String[] lineParts = line.split(",");

        short id = Short.parseShort(lineParts[idPosition]);
        String title = lineParts[titlePosition];
        String description = lineParts[descriptionPosition];
        String status = lineParts[statusPosition];

        switch (lineParts[typePosition]) {
            case "SubTask":
                short epicId = Short.parseShort(lineParts[epicIdPosition]);
                SubTask subtask = new SubTask(id, title, description, status, epicId);
                createSubTask(subtask);
                break;
            case "Task":
                Task task = new Task(id, title, description, status);
                createTask(task);
                break;
            case "Epic":
                Epic epic = new Epic(id, title, description, status);
                createEpic(epic);
                break;
        }
    }
}
