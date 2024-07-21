package lib.managers;

import lib.exceptions.CollisionException;
import lib.exceptions.NotFoundException;
import lib.tasks.*;

import java.time.Duration;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Short, SubTask> subTaskStorage;
    protected final HashMap<Short, Task> taskStorage;
    protected final HashMap<Short, Epic> epicStorage;

    protected final HistoryManager historyManager;

    protected final Set<Task> prioritizedTasks;

    public InMemoryTaskManager() {
        subTaskStorage = new HashMap<>();
        taskStorage = new HashMap<>();
        epicStorage = new HashMap<>();
        historyManager = new InMemoryHistoryManager();
        prioritizedTasks = new TreeSet<>(new StartDateComparator());
    }

    //methods for SubTask

    @Override
    public SubTask getSubTask(short id) throws NotFoundException {
        SubTask subTask = subTaskStorage.get(id);
        if (subTask != null) {
            historyManager.add(subTask);
            return subTask;
        } else {
            throw new NotFoundException("Requested subtask not found.");
        }
    }

    @Override
    public void createSubTask(SubTask subTask) throws CollisionException {
        validateNoCollision(subTask);
        subTaskStorage.put(subTask.id, subTask);
        addToPrioritizedTasksIfStartTimeNotNull(subTask);

        Epic epicOfSubTask = epicStorage.get(subTask.epicId);
        epicOfSubTask.subtasksIds.add(subTask.id);
        setEpicStatus(epicOfSubTask.id);
        setEpicTemporalProperties(epicOfSubTask);

    }

    @Override
    public void deleteSubTask(Short id) throws NotFoundException {
        SubTask subTask = getSubTask(id);
        subTaskStorage.remove(id);

        short epicId = subTask.epicId;
        Epic epic = epicStorage.get(epicId);
        epic.subtasksIds.remove(id);
        setEpicStatus(epicId);
        setEpicTemporalProperties(epic);

        historyManager.remove(id);
        prioritizedTasks.remove(subTask);
    }

    @Override
    public void updateSubTask(SubTask subTask) throws CollisionException, NotFoundException {
        getSubTask(subTask.id);
        validateNoCollision(subTask);
        subTaskStorage.put(subTask.id, subTask);
        prioritizedTasks.remove(subTask);
        addToPrioritizedTasksIfStartTimeNotNull(subTask);

        setEpicStatus(subTask.epicId);
        setEpicTemporalProperties(getEpic(subTask.epicId));
    }

    @Override
    public ArrayList<SubTask> getSubTasksList() {
        return new ArrayList<>(subTaskStorage.values());
    }

    @Override
    public void deleteAllSubTasks() {
        Consumer<SubTask> cleanFunction = subTask -> {
            Epic epicOfSubTask = epicStorage.get(subTask.epicId);
            epicOfSubTask.subtasksIds.clear();
            setEpicStatus(epicOfSubTask.id);
            setEpicTemporalProperties(epicOfSubTask);

            historyManager.remove(subTask.id);
            prioritizedTasks.remove(subTask);
        };
        subTaskStorage.values().stream().forEach(cleanFunction);
        subTaskStorage.clear();
    }

    //methods for Task

    @Override
    public Task getTask(short id) throws NotFoundException {
        Task task = taskStorage.get(id);
        if (task != null) {
            historyManager.add(task);
            return task;
        } else {
            throw new NotFoundException("Requested task not found.");
        }
    }

    @Override
    public void createTask(Task task) throws CollisionException {
        validateNoCollision(task);
        taskStorage.put(task.id, task);
        addToPrioritizedTasksIfStartTimeNotNull(task);
    }

    @Override
    public void deleteTask(short id) throws NotFoundException {
        getTask(id);
        prioritizedTasks.remove(getTask(id));
        taskStorage.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void updateTask(Task updatedTask) throws CollisionException, NotFoundException {
        validateNoCollision(updatedTask);
        deleteTask(updatedTask.id);
        createTask(updatedTask);
    }

    @Override
    public ArrayList<Task> getTasksList() {
        return new ArrayList<>(taskStorage.values());
    }

    @Override
    public void deleteAllTasks() {
        Consumer<Short> cleanFunction = taskId -> {
            prioritizedTasks.remove(getTask(taskId));
            historyManager.remove(taskId);
        };
        taskStorage.keySet().stream().forEach(cleanFunction);

        taskStorage.clear();
    }

    //methods for Epic
    @Override
    public Epic getEpic(short id) throws NotFoundException {
        Epic epic = epicStorage.get(id);
        if (epic != null) {
            historyManager.add(epic);
            return epic;
        } else {
            throw new NotFoundException("Requested epic not found.");
        }
    }

    @Override
    public void createEpic(Epic epic) {
        epic.status = Statuses.NEW;
        epicStorage.put(epic.id, epic);
    }

    @Override
    public void deleteEpic(short id) throws NotFoundException {
        getEpic(id);
        Epic epic = epicStorage.remove(id);
        historyManager.remove(id);

        Consumer<Short> deleteSubtask = subTaskId -> {
            prioritizedTasks.remove(getSubTask(subTaskId));
            subTaskStorage.remove(subTaskId);
            historyManager.remove(subTaskId);
        };
        epic.subtasksIds.stream().forEach(deleteSubtask);
    }

    @Override
    public void updateEpic(Epic updatedEpic) throws NotFoundException {
        Epic oldEpic = getEpic(updatedEpic.id);
        updatedEpic.subtasksIds = oldEpic.subtasksIds;
        updatedEpic.status = oldEpic.status;
        updatedEpic.startTime = oldEpic.startTime;
        updatedEpic.duration = oldEpic.duration;
        updatedEpic.endTime = oldEpic.endTime;

        epicStorage.put(updatedEpic.id, updatedEpic);
    }

    @Override
    public ArrayList<SubTask> subTasksOfEpic(short epicId) throws NotFoundException {
        int initialCapacity = 5;
        ArrayList<SubTask> result = new ArrayList<>(initialCapacity);
        Epic epic = epicStorage.get(epicId);
        if (epic != null) {
            Consumer<Short> addSubTaskToResult = subTaskId -> result.add(subTaskStorage.get(subTaskId));
            epic.subtasksIds.stream().forEach(addSubTaskToResult);
            return result;
        } else {
            throw new NotFoundException("Epic not found.");
        }
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
        Statuses enumStatus = Statuses.valueOf(status);

        Epic epic = epicStorage.get(epicId);
        Predicate<Short> subTaskInStatus = subTaskId -> {
            SubTask subtask = subTaskStorage.get(subTaskId);
            return enumStatus == subtask.status;
        };
        return epic.subtasksIds.stream().allMatch(subTaskInStatus);
    }

    // Метод устанавливает временные свойства эпика на основании временных свойств его подзадач.
    private void setEpicTemporalProperties(Epic epic) {
        List<SubTask> subTasksOfEpic = this.subTasksOfEpic(epic.id);

        epic.duration = Duration.ofMinutes(0);
        Predicate<SubTask> filterTemporalNulls = element -> (element.duration != null) && (element.startTime != null);
        List<SubTask> subTasksWithoutTemporalNulls =
                subTasksOfEpic.stream()
                        .filter(filterTemporalNulls)
                        .peek(element -> epic.duration = epic.duration.plus(element.duration))
                        .collect(Collectors.toList());

        if (subTasksWithoutTemporalNulls.isEmpty()) {
            epic.duration = null;
            epic.startTime = null;
            epic.endTime = null;
        } else {
            Comparator<SubTask> comparator =
                    (subtask1, subtask2) -> subtask1.startTime.isAfter(subtask2.startTime) ? 1 : -1;
            SubTask earliestSubtask = Collections.min(subTasksWithoutTemporalNulls, comparator);
            SubTask latestSubtask = Collections.max(subTasksWithoutTemporalNulls, comparator);
            epic.startTime = earliestSubtask.startTime;
            epic.endTime = latestSubtask.getEndTime();
        }


    }

    @Override
    public ArrayList<Epic> getEpicsList() {
        return new ArrayList<>(epicStorage.values());
    }

    @Override
    public void deleteAllEpics() {
        deleteAllSubTasks();
        epicStorage.keySet().stream().forEach(historyManager::remove);
        epicStorage.clear();
    }

    @Override
    public HistoryManager getHistoryManager() {
        return this.historyManager;
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    private void addToPrioritizedTasksIfStartTimeNotNull(Task task) {
        if (task.startTime != null) {
            prioritizedTasks.add(task);
        }
    }

    private void validateNoCollision(Task inputTask) throws CollisionException {

        boolean collisionIsPresent = prioritizedTasks.stream().anyMatch(
                task -> (task.isTimeCollision(inputTask) && task.id != inputTask.id));

        if (collisionIsPresent) {
            throw new CollisionException(
                    "Task or subTask can not be created because of collision at least with one task/subtask"
            );
        }
    }

}
