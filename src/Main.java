import tasks.Epic;
import tasks.Statuses;
import tasks.SubTask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager();

        // Создание 2-х задач и занесение их в inMemoryTaskManager
        Task taskFst = new Task("Task_1", "Description_1", "NEW");
        Task taskSnd = new Task("Task_2", "Description_2", "NEW");
        inMemoryTaskManager.createTask(taskFst);
        inMemoryTaskManager.createTask(taskSnd);


        // Создание первого эпика и 2-х его подзадач + занесение их в inMemoryTaskManager
        Epic epicFst = new Epic("Epic_1", "Description_3", "NEW");
        SubTask subTaskFst = new SubTask("SubTask_1", "Description_4", "NEW", epicFst.id);
        SubTask subTaskSnd = new SubTask("SubTask_2", "Description_5", "NEW", epicFst.id);
        inMemoryTaskManager.createEpic(epicFst);
        inMemoryTaskManager.createSubTask(subTaskFst);
        inMemoryTaskManager.createSubTask(subTaskSnd);

        // Создание второго эпика и его подзадачи + занесение их в inMemoryTaskManager
        Epic epicSnd = new Epic("Epic_2", "Description_6", "NEW");
        SubTask subTaskThd = new SubTask("SubTask_3", "Description_7", "NEW", epicSnd.id);
        inMemoryTaskManager.createEpic(epicSnd);
        inMemoryTaskManager.createSubTask(subTaskThd);

        // Печать списков эпиков, задач и подзадач
        System.out.println(inMemoryTaskManager.getEpicsList());
        System.out.println(inMemoryTaskManager.getTasksList());
        System.out.println(inMemoryTaskManager.getSubTasksList());

        //Изменение статусов созданных объектов, печать статусов
        // 1) Изменение статусов taskFst и taskSnd
        taskFst.status = Statuses.IN_PROGRESS;
        inMemoryTaskManager.updateTask(taskFst);
        taskSnd.status = Statuses.DONE;
        inMemoryTaskManager.updateTask(taskSnd);

        // 2) Изменение статусов subTaskFst, subTaskSnd, subTaskThd
        subTaskFst.status = Statuses.IN_PROGRESS;
        inMemoryTaskManager.updateSubTask(subTaskFst);
        subTaskSnd.status = Statuses.DONE;
        inMemoryTaskManager.updateSubTask(subTaskSnd);
        subTaskThd.status = Statuses.DONE;
        inMemoryTaskManager.updateSubTask(subTaskThd);

        // 3) Печать статусов всех созданных подзадач/задач/эпиков
        System.out.println(taskFst.status);
        System.out.println(taskSnd.status);
        System.out.println(subTaskFst.status);
        System.out.println(subTaskSnd.status);
        System.out.println(subTaskThd.status);
        System.out.println(epicFst.status);
        System.out.println(epicSnd.status);

        //Пробуем удалить одну из задач и один из эпиков.
        inMemoryTaskManager.deleteTask(taskFst.id);
        inMemoryTaskManager.deleteEpic(epicFst.id);

        System.out.println(inMemoryTaskManager.getSubTasksList());
        System.out.println(inMemoryTaskManager.getTasksList());
        System.out.println(inMemoryTaskManager.getEpicsList());

        inMemoryTaskManager.getTask(inMemoryTaskManager.getTasksList().getFirst().id);
        inMemoryTaskManager.getTask(inMemoryTaskManager.getTasksList().getLast().id);

        inMemoryTaskManager.getSubTask(inMemoryTaskManager.getSubTasksList().getFirst().id);
        inMemoryTaskManager.getSubTask(inMemoryTaskManager.getSubTasksList().getLast().id);

        inMemoryTaskManager.getEpic(inMemoryTaskManager.getEpicsList().getFirst().id);
        inMemoryTaskManager.getEpic(inMemoryTaskManager.getEpicsList().getLast().id);

        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println(inMemoryTaskManager.getHistory());




    }
}
