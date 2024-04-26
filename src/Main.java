import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
       TaskManager taskManager = new TaskManager();

       Epic obj_1 = new Epic("Epic_1", "Some epic business to do!");

     /*  SubTask subTaskFst = new SubTask("SubTask_1 of Epic_1", "111111111", obj_1);

       SubTask subTaskSnd = new SubTask("SubTask_2 of Epic_1", "222222222", obj_1);*/
        Epic obj_2 = new Epic("Epic_2", "Somefff");

       ArrayList<Task> tasksArray = new ArrayList<>();
       tasksArray.add(obj_1);
       tasksArray.add(obj_2);

       taskManager.tasksHashMap.put(TasksType.EPIC, tasksArray);

       taskManager.printTasksOfType("EPIC");


        System.out.println("dfdfff");
        System.out.println(taskManager.getTaskById("EPIC", obj_1.taskId));

        taskManager.deleteTasksOfType("EPIC");

        System.out.println(taskManager.getTaskById("EPIC", obj_1.taskId));




    }
}
