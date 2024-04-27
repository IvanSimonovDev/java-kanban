import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    public HashMap<TasksType, ArrayList<Task>> tasksHashMap;

    public TaskManager() {
        int initialElementsNumber = 5;
        this.tasksHashMap = new HashMap<>();

        for (TasksType taskType : TasksType.values()) {
            ArrayList<Task> tasksOfTypeList = new ArrayList<>(initialElementsNumber);
            this.tasksHashMap.put(taskType, tasksOfTypeList);
        }
    }


    public void deleteTasksOfType(String type) {

        getTasksOfTypeList(type).clear();

        if (TasksType.valueOf(type) == TasksType.SUB_TASK) {
            ArrayList<Task> tasksOfTypeList = getTasksOfTypeList(type);
            for(Task task : tasksOfTypeList) {
                Epic epicTask = (Epic) task;
                setEpicStatus(epicTask);
            }
        }

    }

    public Task getTask(String type, int taskId) {

        Task result = null;
        for(Task task : getTasksOfTypeList(type)) {
            if (task.taskId == taskId) {
                result = task;
                break;
            }

        }
        return result;
    }

    public void removeTask(String type, int taskId) {

        Task taskToRemove = getTask(type, taskId);
        getTasksOfTypeList(type).remove(taskToRemove);

        if (TasksType.valueOf(type) == TasksType.SUB_TASK) {
            SubTask subTask = (SubTask) taskToRemove;
            Epic epicOfSubTask = subTask.epicObject;
            epicOfSubTask.subTasksList.remove(subTask);
            setEpicStatus(epicOfSubTask);
        } else if (TasksType.valueOf(type) == TasksType.EPIC) {
              Epic epic = (Epic) taskToRemove;
              ArrayList<Task> subTasksList = getTasksOfTypeList("SUB_TASK");
              for (SubTask subTask : epic.subTasksList) {
                  subTasksList.remove(subTask);
              }
        }

    }
    public void addTask(String type, Task newTask) {

        getTasksOfTypeList(type).add(newTask);

        if (TasksType.valueOf(type) == TasksType.SUB_TASK) {
            SubTask subTask = (SubTask) newTask;
            Epic epicOfSubTask = subTask.epicObject;
            if (!epicOfSubTask.subTasksList.contains(subTask)) {
                epicOfSubTask.subTasksList.add(subTask);
            }
            setEpicStatus(epicOfSubTask);
        }

    }

    public void changeTask(String type, int taskId, Task updatedTask) {

        removeTask(type, taskId);
        addTask(type, updatedTask);

    }

    public ArrayList<SubTask> getSubTasksList(int epicId) {
        Task rawEpic = getTask(TasksType.EPIC.name(), epicId);
        Epic epic = (Epic) rawEpic;
        return epic.subTasksList;

    }

    public ArrayList<Task> getTasksOfTypeList(String type) {

        TasksType enumType = TasksType.valueOf(type);
        return tasksHashMap.get(enumType);

    }

    public void setEpicStatus(Epic epic) {
        if (epic.areAllSubTasksInStatus("NEW") || epic.subTasksList.isEmpty()) {
            epic.setStatus("NEW");
        } else if ( epic.areAllSubTasksInStatus("DONE") ) {
              epic.setStatus("DONE");
        } else {
              epic.setStatus("IN_PROGRESS");
        }
    }

}

enum TasksType {
    SUB_TASK,
    TASK,
    EPIC
}
