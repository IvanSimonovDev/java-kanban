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

    public void printTasksOfType(String type) {
        TasksType enumType = TasksType.valueOf(type);
        ArrayList<Task> tasksOfTypeList = tasksHashMap.get(enumType);

        for(Task task : tasksOfTypeList) {
            System.out.println(task);
        }

    }

    public void deleteTasksOfType(String type) {
        TasksType enumType = TasksType.valueOf(type);
        ArrayList<Task> tasksOfTypeList = tasksHashMap.get(enumType);

        tasksOfTypeList.clear();

    }

    public Task getTaskById(String type, int taskId) {
        TasksType enumType = TasksType.valueOf(type);
        ArrayList<Task> tasksOfTypeList = tasksHashMap.get(enumType);
        Task result = null;
        for(Task task : tasksOfTypeList) {
            if (task.taskId == taskId) {
                result = task;
                break;
            }

        }
        return result;
    }

}

enum TasksType {
    SUB_TASK,
    TASK,
    EPIC
}
