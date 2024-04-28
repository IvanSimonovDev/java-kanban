import tasks.Epic;
import tasks.Statuses;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TaskManager {
    final HashMap<Short, SubTask> subTaskStorage;
    final HashMap<Short, Task> taskStorage;
    final HashMap<Short, Epic> epicStorage;

    public TaskManager() {
        int initialCapacity = 10;
        subTaskStorage = new HashMap<>(initialCapacity);
        taskStorage = new HashMap<>(initialCapacity);
        epicStorage = new HashMap<>(initialCapacity);
    }

    //methods for SubTask

    public SubTask getSubTask(short id) {
        return subTaskStorage.get(id);
    }

    public void createSubTask(SubTask subTask) {
        subTaskStorage.put(subTask.id, subTask);
        Epic epicOfSubTask = getEpic(subTask.epicId);
        epicOfSubTask.subtasksIds.add(subTask.id);
        setEpicStatus(epicOfSubTask.id);
    }

    public void deleteSubTask(Short id) {
        SubTask subTask = getSubTask(id);
        subTaskStorage.remove(id);
        short epicId = subTask.epicId;
        Epic epic = getEpic(epicId);
        epic.subtasksIds.remove(id);
        setEpicStatus(epicId);
    }

    public void updateSubTask(SubTask subTask) {
        deleteSubTask(subTask.id);
        createSubTask(subTask);
        setEpicStatus(subTask.epicId);
    }

    public Collection<SubTask> getSubTasksList() {
        return subTaskStorage.values();
    }

    public void deleteAllSubTasks() {
        for (SubTask subTask : subTaskStorage.values()) {
            Epic epicOfSubTask = getEpic(subTask.epicId);
            epicOfSubTask.subtasksIds.clear();
            setEpicStatus(epicOfSubTask.id);
        }
        subTaskStorage.clear();
    }

    //methods for Task

    public Task getTask(short id) {
        return taskStorage.get(id);
    }

    public void createTask(Task task) {
        taskStorage.put(task.id, task);
    }

    public void deleteTask(short id) {
        taskStorage.remove(id);
    }

    public void updateTask(Task updatedTask) {
        deleteTask(updatedTask.id);
        createTask(updatedTask);
    }

    public Collection<Task> getTasksList() {
        return taskStorage.values();
    }

    public void deleteAllTasks() {
        taskStorage.clear();
    }

    //methods for Epic

    public Epic getEpic(short id) {
        return epicStorage.get(id);
    }

    public void createEpic(Epic epic) {
        epicStorage.put(epic.id, epic);
    }

    public void deleteEpic(short id) {
        Epic epic = getEpic(id);

        int initialCapacity = 5;
        ArrayList<Short> subtasksIdsCopy = new ArrayList<>(initialCapacity);
        for (short subTaskId : epic.subtasksIds) {
            subtasksIdsCopy.add(subTaskId);
        }

        for (short subTaskId : subtasksIdsCopy) {
            deleteSubTask(subTaskId);
        }

        epicStorage.remove(id);
    }

    public void updateEpic(Epic updatedEpic) {
        Epic oldEpic = getEpic(updatedEpic.id);
        updatedEpic.subtasksIds = oldEpic.subtasksIds;
        deleteEpic(updatedEpic.id);
        createEpic(updatedEpic);
        setEpicStatus(updatedEpic.id);
    }

    public ArrayList<SubTask> subTasksOfEpic(short epicId) {
        int initialCapacity = 5;
        ArrayList<SubTask> result = new ArrayList<>(initialCapacity);
        Epic epic = getEpic(epicId);
        for (Short subTaskId : epic.subtasksIds) {
            result.add(getSubTask(subTaskId));
        }
        return result;
    }

    private void setEpicStatus(short epicId) {
        Epic epic = getEpic(epicId);

        if (epic.subtasksIds.isEmpty() || areAllSubTasksInStatus("NEW", epicId)) {
            epic.status = Statuses.NEW;
        } else if (areAllSubTasksInStatus("DONE", epicId)) {
            epic.status = Statuses.DONE;
        } else {
            epic.status = Statuses.IN_PROGRESS;
        }

    }

    private boolean areAllSubTasksInStatus(String status, short epicId) {
        boolean result = true;
        Statuses enumStatus = Statuses.valueOf(status);

        Epic epic = getEpic(epicId);
        for (Short subTaskId : epic.subtasksIds) {
            SubTask subtask = getSubTask(subTaskId);
            Statuses enumStatusOfSubTask = subtask.status;
            result = result && (enumStatus == enumStatusOfSubTask);
        }
        return result;
    }

    public Collection<Epic> getEpicsList() {
        return epicStorage.values();
    }

    public void deleteAllEpics() {
        subTaskStorage.clear();
        epicStorage.clear();
    }


}
