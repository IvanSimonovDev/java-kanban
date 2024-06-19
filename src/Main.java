import lib.HistoryManager;
import lib.Managers;
import lib.TaskManager;
import lib.tasks.Epic;
import lib.tasks.SubTask;
import lib.tasks.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        HistoryManager inMemoryHistoryManager = inMemoryTaskManager.getHistoryManager();

        // Создание 2-х задач и занесение их в inMemoryTaskManager
        Task taskFst = new Task("Task_1", "Description_1", "NEW");
        Task taskSnd = new Task("Task_2", "Description_2", "NEW");
        inMemoryTaskManager.createTask(taskFst);
        inMemoryTaskManager.createTask(taskSnd);


        // Создание первого эпика и 3-х его подзадач + занесение их в inMemoryTaskManager
        Epic epicFst = new Epic("Epic_1", "Description_3", "NEW");
        SubTask subTaskFst = new SubTask("SubTask_1", "Description_4", "NEW", epicFst.id);
        SubTask subTaskSnd = new SubTask("SubTask_2", "Description_5", "NEW", epicFst.id);
        SubTask subTaskThd = new SubTask("SubTask_3", "Description_6", "NEW", epicFst.id);
        inMemoryTaskManager.createEpic(epicFst);
        inMemoryTaskManager.createSubTask(subTaskFst);
        inMemoryTaskManager.createSubTask(subTaskSnd);
        inMemoryTaskManager.createSubTask(subTaskThd);

        // Создание второго эпика  + занесение его в inMemoryTaskManager
        Epic epicSnd = new Epic("Epic_2", "Description_7", "NEW");
        inMemoryTaskManager.createEpic(epicSnd);

        // Запрос созданных задач несколько раз в разном порядке.
        inMemoryTaskManager.getTask(taskSnd.id);
        System.out.println(inMemoryHistoryManager.getHistory());

        inMemoryTaskManager.getTask(taskFst.id);
        System.out.println(inMemoryHistoryManager.getHistory());

        inMemoryTaskManager.getSubTask(subTaskThd.id);
        System.out.println(inMemoryHistoryManager.getHistory());

        inMemoryTaskManager.getEpic(epicSnd.id);
        System.out.println(inMemoryHistoryManager.getHistory());

        inMemoryTaskManager.getTask(taskFst.id);
        System.out.println(inMemoryHistoryManager.getHistory());

        inMemoryTaskManager.getTask(taskSnd.id);
        System.out.println(inMemoryHistoryManager.getHistory());

        inMemoryTaskManager.getSubTask(subTaskThd.id);
        System.out.println(inMemoryHistoryManager.getHistory());

        inMemoryTaskManager.getSubTask(subTaskFst.id);
        System.out.println(inMemoryHistoryManager.getHistory());

        inMemoryTaskManager.getEpic(epicFst.id);
        System.out.println(inMemoryHistoryManager.getHistory());

        inMemoryTaskManager.getSubTask(subTaskSnd.id);
        System.out.println(inMemoryHistoryManager.getHistory());

        inMemoryTaskManager.getEpic(epicFst.id);
        System.out.println(inMemoryHistoryManager.getHistory());

        inMemoryTaskManager.getSubTask(subTaskFst.id);
        System.out.println(inMemoryHistoryManager.getHistory());

        inMemoryTaskManager.getTask(taskSnd.id);
        System.out.println(inMemoryHistoryManager.getHistory());

        inMemoryTaskManager.getTask(taskFst.id);
        System.out.println(inMemoryHistoryManager.getHistory());

        inMemoryTaskManager.getSubTask(subTaskThd.id);
        System.out.println(inMemoryHistoryManager.getHistory());

        inMemoryTaskManager.getSubTask(subTaskSnd.id);
        System.out.println(inMemoryHistoryManager.getHistory());


        //Удаление задачи, которая есть в истории, и проверка, что при печати она не будет выводиться
        inMemoryTaskManager.deleteSubTask(subTaskSnd.id);
        System.out.println(inMemoryHistoryManager.getHistory());

        //Удаление эпика с тремя подзадачами и просмотр истории
        inMemoryTaskManager.deleteEpic(epicFst.id);
        System.out.println(inMemoryHistoryManager.getHistory());


    }
}
