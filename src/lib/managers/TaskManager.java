package lib.managers;

import lib.exceptions.CollisionException;
import lib.exceptions.NotFoundException;
import lib.tasks.Epic;
import lib.tasks.SubTask;
import lib.tasks.Task;

import java.util.ArrayList;
import java.util.Set;

public interface TaskManager {

    //methods for SubTask
    SubTask getSubTask(short id) throws NotFoundException;

    void createSubTask(SubTask subTask) throws CollisionException;

    void deleteSubTask(Short id) throws NotFoundException;

    void updateSubTask(SubTask subTask) throws NotFoundException, CollisionException;

    ArrayList<SubTask> getSubTasksList();

    void deleteAllSubTasks();

    //methods for Task

    Task getTask(short id) throws NotFoundException;

    void createTask(Task task) throws CollisionException;

    void deleteTask(short id) throws NotFoundException;

    void updateTask(Task updatedTask) throws NotFoundException, CollisionException;

    ArrayList<Task> getTasksList();

    void deleteAllTasks();

    //methods for Epic

    Epic getEpic(short id) throws NotFoundException;

    void createEpic(Epic epic);

    void deleteEpic(short id) throws NotFoundException;

    void updateEpic(Epic updatedEpic) throws NotFoundException;

    ArrayList<SubTask> subTasksOfEpic(short epicId) throws NotFoundException;

    ArrayList<Epic> getEpicsList();

    void deleteAllEpics();

    //general method
    HistoryManager getHistoryManager();

    Set<Task> getPrioritizedTasks();
}
