package lib;

import lib.tasks.Epic;
import lib.tasks.Statuses;
import lib.tasks.SubTask;
import lib.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Short, SubTask> subTaskStorage;
    protected final HashMap<Short, Task> taskStorage;
    protected final HashMap<Short, Epic> epicStorage;

    protected final HistoryManager historyManager;

    public InMemoryTaskManager() {
        subTaskStorage = new HashMap<>();
        taskStorage = new HashMap<>();
        epicStorage = new HashMap<>();
        historyManager = new InMemoryHistoryManager();
    }

    //methods for SubTask

    @Override
    public SubTask getSubTask(short id) {
        SubTask subTask = subTaskStorage.get(id);
        historyManager.add(subTask);
        return subTask;
    }

    @Override
    public void createSubTask(SubTask subTask) {
        subTaskStorage.put(subTask.id, subTask);
        Epic epicOfSubTask = epicStorage.get(subTask.epicId);
        epicOfSubTask.subtasksIds.add(subTask.id);
        setEpicStatus(epicOfSubTask.id);
    }

    @Override
    public void deleteSubTask(Short id) {
        SubTask subTask = subTaskStorage.get(id);
        subTaskStorage.remove(id);
        short epicId = subTask.epicId;
        Epic epic = epicStorage.get(epicId);
        epic.subtasksIds.remove(id);
        setEpicStatus(epicId);

        historyManager.remove(id);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        subTaskStorage.put(subTask.id, subTask);
        setEpicStatus(subTask.epicId);
    }

    @Override
    public ArrayList<SubTask> getSubTasksList() {
        return new ArrayList<>(subTaskStorage.values());
    }

    @Override
    public void deleteAllSubTasks() {
        for (SubTask subTask : subTaskStorage.values()) {
            Epic epicOfSubTask = epicStorage.get(subTask.epicId);
            epicOfSubTask.subtasksIds.clear();
            setEpicStatus(epicOfSubTask.id);

            historyManager.remove(subTask.id);
        }
        subTaskStorage.clear();
    }

    //methods for Task

    @Override
    public Task getTask(short id) {
        Task task = taskStorage.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public void createTask(Task task) {
        taskStorage.put(task.id, task);
    }

    @Override
    public void deleteTask(short id) {

        taskStorage.remove(id);

        historyManager.remove(id);
    }

    @Override
    public void updateTask(Task updatedTask) {
        deleteTask(updatedTask.id);
        createTask(updatedTask);
    }

    @Override
    public ArrayList<Task> getTasksList() {
        return new ArrayList<>(taskStorage.values());
    }

    @Override
    public void deleteAllTasks() {
        for (Short taskId : taskStorage.keySet()) {
            historyManager.remove(taskId);
        }

        taskStorage.clear();
    }

    //methods for Epic

    @Override
    public Epic getEpic(short id) {
        Epic epic = epicStorage.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public void createEpic(Epic epic) {
        epicStorage.put(epic.id, epic);
    }

    @Override
    public void deleteEpic(short id) {
        Epic epic = epicStorage.remove(id);
        historyManager.remove(id);

        for (short subTaskId : epic.subtasksIds) {
            subTaskStorage.remove(subTaskId);
            historyManager.remove(subTaskId);
        }
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
        epicStorage.put(updatedEpic.id, updatedEpic);
    }

    @Override
    public ArrayList<SubTask> subTasksOfEpic(short epicId) {
        int initialCapacity = 5;
        ArrayList<SubTask> result = new ArrayList<>(initialCapacity);
        Epic epic = epicStorage.get(epicId);
        for (Short subTaskId : epic.subtasksIds) {
            result.add(subTaskStorage.get(subTaskId));
        }
        return result;
    }

    protected void setEpicStatus(short epicId) {
        Epic epic = epicStorage.get(epicId);

        if (epic.subtasksIds.isEmpty() || areAllSubTasksInStatus("NEW", epicId)) {
            epic.status = Statuses.NEW;
        } else if (areAllSubTasksInStatus("DONE", epicId)) {
            epic.status = Statuses.DONE;
        } else {
            epic.status = Statuses.IN_PROGRESS;
        }

    }

    protected boolean areAllSubTasksInStatus(String status, short epicId) {
        boolean result = true;
        Statuses enumStatus = Statuses.valueOf(status);

        Epic epic = epicStorage.get(epicId);
        for (Short subTaskId : epic.subtasksIds) {
            SubTask subtask = subTaskStorage.get(subTaskId);
            Statuses enumStatusOfSubTask = subtask.status;
            result = result && (enumStatus == enumStatusOfSubTask);
        }
        return result;
    }

    @Override
    public ArrayList<Epic> getEpicsList() {
        return new ArrayList<>(epicStorage.values());
    }

    @Override
    public void deleteAllEpics() {
        deleteAllSubTasks();
        for (Short epicId : epicStorage.keySet()) {
            historyManager.remove(epicId);
        }
        epicStorage.clear();
    }

    @Override
    public HistoryManager getHistoryManager() {
        return this.historyManager;
    }

}
