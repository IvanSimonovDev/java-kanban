import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        // Создание 2-х задач и занесение их в TaskManager
        Task taskFst = new Task("Task_1", "Description_1", "NEW");
        Task taskSnd = new Task("Task_2", "Description_2", "NEW");
        taskManager.addTask("TASK", taskFst);
        taskManager.addTask("TASK", taskSnd);

        // Создание эпика и 2-х его подзадач + занесение их в TaskManager
        Epic epicFst = new Epic("Epic_1", "Epic_description_1",new ArrayList<>());
        SubTask subTaskFst = new SubTask("SubTask_1", "SubTask_Description_1", "NEW", epicFst);
        SubTask subTaskSnd = new SubTask("SubTask_2", "SubTask_Description_2", "NEW", epicFst);
        taskManager.addTask("EPIC", epicFst);
        taskManager.addTask("SUB_TASK", subTaskFst);
        taskManager.addTask("SUB_TASK", subTaskSnd);

        // Создание эпика и его подзадачи + занесение их в TaskManager
        Epic epicSnd = new Epic("Epic_2", "Epic_description_2",new ArrayList<>());
        SubTask subTaskThd = new SubTask("SubTask_3", "SubTask_Description_3", "NEW", epicSnd);
        taskManager.addTask("EPIC", epicSnd);
        taskManager.addTask("SUB_TASK", subTaskThd);

        // Печать списков эпиков, задач и подзадач
        System.out.println(taskManager.getTasksOfTypeList("TASK"));
        System.out.println(taskManager.getTasksOfTypeList("SUB_TASK"));
        System.out.println(taskManager.getTasksOfTypeList("EPIC") + "\n");

        //Изменение статусов созданных объектов, печать статусов
        taskFst.setStatus("DONE");
        taskSnd.setStatus("IN_PROGRESS");
        taskManager.changeTask("TASK", taskFst.taskId, taskFst);
        taskManager.changeTask("TASK", taskSnd.taskId, taskSnd);
        System.out.println(taskFst.status);
        System.out.println(taskSnd.status + "\n");

        subTaskFst.setStatus("DONE");
        subTaskSnd.setStatus("IN_PROGRESS");
        subTaskThd.setStatus("DONE");
        taskManager.changeTask("SUB_TASK", subTaskFst.taskId, subTaskFst);
        taskManager.changeTask("SUB_TASK", subTaskSnd.taskId, subTaskSnd);
        taskManager.changeTask("SUB_TASK", subTaskThd.taskId, subTaskThd);
        System.out.println(subTaskFst.status);
        System.out.println(subTaskSnd.status);
        System.out.println(subTaskThd.status + "\n");

        System.out.println(epicFst.status);
        System.out.println(epicSnd.status);

        //Пробуем удалить одну из задач и один из эпиков.
        taskManager.removeTask("TASK", taskFst.taskId);
        taskManager.removeTask("SUB_TASK", subTaskSnd.taskId);
        System.out.println(taskManager.getTasksOfTypeList("TASK"));
        System.out.println(taskManager.getTasksOfTypeList("SUB_TASK"));
        System.out.println(taskManager.getTasksOfTypeList("EPIC") + "\n");


        System.out.println(epicFst.status);








    }
}
