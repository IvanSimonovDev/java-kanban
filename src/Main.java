import lib.FileBackedTaskManager;
import lib.TaskManager;
import lib.tasks.Epic;
import lib.tasks.SubTask;
import lib.tasks.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager fstTaskManager = new FileBackedTaskManager();

        // Создание 2-х задач и занесение их в fstTaskManager
        Task taskFst = new Task("Task_1", "Description_1", "NEW");
        Task taskSnd = new Task("Task_2", "Description_2", "NEW");
        fstTaskManager.createTask(taskFst);
        fstTaskManager.createTask(taskSnd);


        // Создание первого эпика и 3-х его подзадач + занесение их в fstTaskManager
        Epic epicFst = new Epic("Epic_1", "Description_3", "NEW");
        SubTask subTaskFst = new SubTask("SubTask_1", "Description_4", "NEW", epicFst.id);
        SubTask subTaskSnd = new SubTask("SubTask_2", "Description_5", "NEW", epicFst.id);
        SubTask subTaskThd = new SubTask("SubTask_3", "Description_6", "NEW", epicFst.id);
        fstTaskManager.createEpic(epicFst);
        fstTaskManager.createSubTask(subTaskFst);
        fstTaskManager.createSubTask(subTaskSnd);
        fstTaskManager.createSubTask(subTaskThd);

        // Создание второго эпика  + занесение его в fstTaskManager
        Epic epicSnd = new Epic("Epic_2", "Description_7", "NEW");
        fstTaskManager.createEpic(epicSnd);

        // Создание второго менеджера и загрузка его из файла по умолчанию
        TaskManager sndTaskManager = new FileBackedTaskManager();

        //Проверка на совпадение списков задач
        boolean conditionFst = fstTaskManager.getTasksList().equals(sndTaskManager.getTasksList());
        boolean conditionSnd = fstTaskManager.getSubTasksList().equals(sndTaskManager.getSubTasksList());
        boolean conditionThd = fstTaskManager.getEpicsList().equals(sndTaskManager.getEpicsList());

        System.out.println(conditionFst && conditionSnd && conditionThd); // true
    }
}
