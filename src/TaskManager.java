import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.List;
import java.util.ArrayList;

public interface TaskManager {

    //methods for SubTask
    SubTask getSubTask(short id);

    void createSubTask(SubTask subTask);

    void deleteSubTask(Short id);

    void updateSubTask(SubTask subTask);

    ArrayList<SubTask> getSubTasksList();

    void deleteAllSubTasks();

    //methods for Task

    Task getTask(short id);

    void createTask(Task task);

    void deleteTask(short id);

    void updateTask(Task updatedTask);

    ArrayList<Task> getTasksList();

    void deleteAllTasks();

    //methods for Epic

    Epic getEpic(short id);

    void createEpic(Epic epic);

    void deleteEpic(short id);

    void updateEpic(Epic updatedEpic);

    ArrayList<SubTask> subTasksOfEpic(short epicId);

    ArrayList<Epic> getEpicsList();

    void deleteAllEpics();

    //general method

    List<Task> getHistory();

}
