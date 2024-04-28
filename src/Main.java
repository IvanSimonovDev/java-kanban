import tasks.Epic;
import tasks.Statuses;
import tasks.SubTask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        // Создание 2-х задач и занесение их в TaskManager
        Task taskFst = new Task("Task_1", "Description_1", "NEW");
        Task taskSnd = new Task("Task_2", "Description_2", "NEW");
        taskManager.createTask(taskFst);
        taskManager.createTask(taskSnd);


        // Создание первого эпика и 2-х его подзадач + занесение их в TaskManager
        Epic epicFst = new Epic("Epic_1", "Description_3", "NEW");
        SubTask subTaskFst = new SubTask("SubTask_1", "Description_4", "NEW", epicFst.id);
        SubTask subTaskSnd = new SubTask("SubTask_2", "Description_5", "NEW", epicFst.id);
        taskManager.createEpic(epicFst);
        taskManager.createSubTask(subTaskFst);
        taskManager.createSubTask(subTaskSnd);

        // Создание второго эпика и его подзадачи + занесение их в TaskManager
        Epic epicSnd = new Epic("Epic_2", "Description_6", "NEW");
        SubTask subTaskThd = new SubTask("SubTask_3", "Description_7", "NEW", epicSnd.id);
        taskManager.createEpic(epicSnd);
        taskManager.createSubTask(subTaskThd);

        // Печать списков эпиков, задач и подзадач
        System.out.println(taskManager.getEpicsList());
        System.out.println(taskManager.getTasksList());
        System.out.println(taskManager.getSubTasksList());

        //Изменение статусов созданных объектов, печать статусов
        // 1) Изменение статусов taskFst и taskSnd
        taskFst.status = Statuses.IN_PROGRESS;
        taskManager.updateTask(taskFst);
        taskSnd.status = Statuses.DONE;
        taskManager.updateTask(taskSnd);

        // 2) Изменение статусов subTaskFst, subTaskSnd, subTaskThd
        subTaskFst.status = Statuses.IN_PROGRESS;
        taskManager.updateSubTask(subTaskFst);
        subTaskSnd.status = Statuses.DONE;
        taskManager.updateSubTask(subTaskSnd);
        subTaskThd.status = Statuses.DONE;
        taskManager.updateSubTask(subTaskThd);

        // 3) Печать статусов всех созданных подзадач/задач/эпиков
        System.out.println(taskFst.status);
        System.out.println(taskSnd.status);
        System.out.println(subTaskFst.status);
        System.out.println(subTaskSnd.status);
        System.out.println(subTaskThd.status);
        System.out.println(epicFst.status);
        System.out.println(epicSnd.status);

        //Пробуем удалить одну из задач и один из эпиков.
        taskManager.deleteTask(taskFst.id);
        taskManager.deleteEpic(epicFst.id);

        System.out.println(taskManager.getSubTasksList());
        System.out.println(taskManager.getTasksList());
        System.out.println(taskManager.getEpicsList());


    }
}
