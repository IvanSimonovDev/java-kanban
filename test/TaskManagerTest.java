import lib.InMemoryTaskManager;
import lib.tasks.Epic;
import lib.tasks.Statuses;
import lib.tasks.SubTask;
import lib.tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

abstract class TaskManagerTest<T extends InMemoryTaskManager> {

    T taskManager;
    private Task task;
    private SubTask subTask;
    private Epic epic;

    static final short EPIC_ID = 1;
    static final short SUBTASK_ID = 2;
    static final short TASK_ID = 15;

    @BeforeEach
    public abstract void taskManagerCreation();

    @BeforeEach
    public void makeTaskAndSubTaskAndEpicNull() {
        task = null;
        subTask = null;
        epic = null;
    }

    @Test
    public void canCreateSubTaskAndFindItById() {
        createEpicAndItsSubtaskWithoutTime(EPIC_ID, SUBTASK_ID);
        SubTask returnedSubTask = taskManager.getSubTask(subTask.id);
        Assertions.assertEquals(subTask, returnedSubTask);
    }

    @Test
    public void canUpdateSubTaskAndDeleteIt() {
        createEpicAndItsSubtaskWithoutTime(EPIC_ID, SUBTASK_ID);

        String newTitle = "Updated subTask_1";
        SubTask updatedSubTask = new SubTask(
                SUBTASK_ID,
                newTitle,
                "Description of SubTask_1",
                "NEW",
                EPIC_ID
        );
        taskManager.updateSubTask(updatedSubTask);
        boolean condition1 = taskManager.getSubTask(SUBTASK_ID).title.equals(newTitle);

        taskManager.deleteAllSubTasks();
        boolean condition2 = taskManager.getSubTasksList().isEmpty();

        Assertions.assertTrue(condition1 && condition2);
    }

    public void createEpicAndItsSubtaskWithoutTime(short epicId, short subTaskId) {
        epic = new Epic(epicId, "Epic_1", "Description of Epic_1", "NEW");
        subTask = new SubTask(
                subTaskId,
                "SubTask_1",
                "Description of SubTask_1",
                "NEW",
                epic.id
        );

        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);
    }

    @Test
    public void subTaskFieldsCanChangeOutsideOfTaskManager() {
        short subTaskId = 22;
        short epicId = 33;
        SubTask subTask = new SubTask(subTaskId, "SubTask_1", "Description of SubTask_1",
                "NEW", epicId);

        short newSubTaskId = 11;
        String newSubTaskTitle = "SubTask_2";
        String newSubTaskDescription = "Description of SubTask_2";
        Statuses newSubTaskStatus = Statuses.IN_PROGRESS;
        short newEpicId = 44;

        subTask.id = newSubTaskId;
        subTask.title = newSubTaskTitle;
        subTask.description = newSubTaskDescription;
        subTask.status = newSubTaskStatus;
        subTask.epicId = newEpicId;

        boolean conditionFst = (subTask.id == newSubTaskId);
        boolean conditionSnd = (subTask.title.equals(newSubTaskTitle));
        boolean conditionThd = (subTask.description.equals(newSubTaskDescription));
        boolean conditionFth = (subTask.status == newSubTaskStatus);
        boolean conditionFifth = (subTask.epicId == newEpicId);

        Assertions.assertTrue(conditionFst && conditionSnd && conditionThd && conditionFth && conditionFifth);

    }

    @Test
    public void subTaskShouldHaveEpic() {
        createEpicAndItsSubtaskWithoutTime(EPIC_ID, TASK_ID);

        Assertions.assertSame(taskManager.getEpic(subTask.epicId), epic);
    }

    public void createTaskWithoutTime(short taskId) {
        task = new Task(taskId, "Task_1", "Description of Task_1", "NEW");
        taskManager.createTask(task);
    }

    @Test
    public void canCreateTaskAndFindItById() {

        createTaskWithoutTime(TASK_ID);

        Task returnedTask = taskManager.getTask(task.id);

        Assertions.assertEquals(task, returnedTask);
    }

    @Test
    public void canDeleteTaskInTwoWays() {
        createTaskWithoutTime(TASK_ID);
        taskManager.deleteAllTasks();
        boolean condition1 = taskManager.getTasksList().isEmpty();

        createTaskWithoutTime(TASK_ID);
        taskManager.deleteTask(TASK_ID);
        boolean condition2 = taskManager.getTasksList().isEmpty();

        Assertions.assertTrue(condition1 && condition2);
    }

    @Test
    public void TaskWithSetIdAndTaskWithGeneratedIdDoNotConflict() {
        createTaskWithoutTime(TASK_ID);

        Task updatedTask = new Task(TASK_ID, "Another Task_1", "Description of Task_1", "DONE");
        taskManager.updateTask(updatedTask);

        List<Task> listOfTasks = taskManager.getTasksList();

        boolean condition = (listOfTasks.size() == 1) &&
                taskManager.getTask(updatedTask.id).title.equals("Another Task_1");

        Assertions.assertTrue(condition);
    }

    @Test
    public void TaskDoesNotChangeAfterAddingToManager() {
        createTaskWithoutTime(TASK_ID);
        Task returnedTask = taskManager.getTask(TASK_ID);
        boolean condition = (task.id == returnedTask.id) && (task.title.equals(returnedTask.title)) &&
                (task.description.equals(returnedTask.description)) && (task.status.equals(returnedTask.status));

        Assertions.assertTrue(condition);
    }


    @Test
    public void taskFieldsCanChangeOutsideOfTaskManager() {
        createTaskWithoutTime(TASK_ID);

        short newTaskId = 11;
        String newTaskTitle = "Task_2";
        String newTaskDescription = "Description of Task_2";
        Statuses newTaskStatus = Statuses.IN_PROGRESS;

        task.id = newTaskId;
        task.title = newTaskTitle;
        task.description = newTaskDescription;
        task.status = newTaskStatus;

        boolean conditionFst = (task.id == newTaskId);
        boolean conditionSnd = (task.title.equals(newTaskTitle));
        boolean conditionThd = (task.description.equals(newTaskDescription));
        boolean conditionFth = (task.status == newTaskStatus);

        Assertions.assertTrue(conditionFst && conditionSnd && conditionThd && conditionFth);
    }

    @Test
    public void canCreateEpicAndFindItById() {

        Epic epic = new Epic("Epic_1", "Description of Epic_1", "NEW");

        taskManager.createEpic(epic);

        Epic returnedEpic = taskManager.getEpic(epic.id);

        Assertions.assertEquals(epic, returnedEpic);
    }

    @Test
    public void canUpdateEpicAndGetItsSubTasks() {
        createEpicAndItsSubtaskWithoutTime(EPIC_ID, SUBTASK_ID);

        String newTitle = "Updated_Epic_1";
        Epic updatedEpic = new Epic(epic.id, newTitle, epic.description, epic.status.toString());
        taskManager.updateEpic(updatedEpic);
        boolean condition1 = taskManager.getEpic(epic.id).title.equals(newTitle);

        int subTasksInEpic = 1;
        boolean condition2 = (taskManager.subTasksOfEpic(epic.id).size() == subTasksInEpic) &&
                taskManager.subTasksOfEpic(epic.id).contains(subTask);

        Assertions.assertTrue(condition1);
    }

    @Test
    public void canGetEpicsListAndDeleteAllEpicsWithOneMethod() {
        createEpicAndItsSubtaskWithoutTime(EPIC_ID, SUBTASK_ID);
        boolean condition1 = taskManager.getEpicsList().size() == 1;

        taskManager.deleteAllEpics();
        boolean condition2 = taskManager.getEpicsList().isEmpty();

        Assertions.assertTrue(condition1 && condition2);
    }

    @Test
    public void epicFieldsCanChangeOutsideOfTaskManager() {
        short epicId = 22;
        Epic epic = new Epic(epicId, "Epic_1", "Description of Epic_1", "NEW");

        short newEpicId = 11;
        String newEpicTitle = "Epic_2";
        String newEpicDescription = "Description of Epic_2";
        Statuses newEpicStatus = Statuses.IN_PROGRESS;
        ArrayList<Short> newSubtasksIds = new ArrayList<>(List.of(epicId));

        epic.id = newEpicId;
        epic.title = newEpicTitle;
        epic.description = newEpicDescription;
        epic.status = newEpicStatus;
        epic.subtasksIds = newSubtasksIds;

        boolean conditionFst = (epic.id == newEpicId);
        boolean conditionSnd = (epic.title.equals(newEpicTitle));
        boolean conditionThd = (epic.description.equals(newEpicDescription));
        boolean conditionFth = (epic.status == newEpicStatus);
        boolean conditionFifth = (epic.subtasksIds.equals(newSubtasksIds));

        Assertions.assertTrue(conditionFst && conditionSnd && conditionThd && conditionFth && conditionFifth);
    }

    @Test
    public void epicShouldNotContainDeletedSubTask() {

        Epic epicOfSubTask = new Epic("Epic_1", "Description of Epic_1", "NEW");
        SubTask subTask = new SubTask("SubTask_1", "Description of SubTask_1", "NEW", epicOfSubTask.id);

        taskManager.createEpic(epicOfSubTask);
        taskManager.createSubTask(subTask);
        taskManager.deleteSubTask(subTask.id);

        Assertions.assertTrue(epicOfSubTask.subtasksIds.isEmpty());

    }

    @Test
    public void epicTemporalPropertiesFormCorrectly() {
        short epicId = 1;
        Epic epic = new Epic(epicId, "Epic_1", "Description_1", "NEW");

        //создание объекта первого эпика
        String subtaskFstStringStartTime = "2007-09-01T21:00";
        LocalDateTime subtaskFstStartTime = LocalDateTime.parse(subtaskFstStringStartTime);
        int subTaskFstDurationInMinutes = 60;
        Duration subTaskFstDuration = Duration.ofMinutes(subTaskFstDurationInMinutes);
        SubTask subTaskFst = new SubTask(
                (short) 2,
                "Subtask_1",
                "Description_2",
                "NEW",
                epicId,
                subtaskFstStartTime,
                subTaskFstDuration
        );

        //создание объекта второй подзадачи эпика (без указания времени и продолжительности)
        SubTask subTaskSnd = new SubTask(
                (short) 3,
                "Subtask_2",
                "Description_3",
                "NEW",
                epicId
        );

        //создание объекта третьей подзадачи эпика
        String subtaskThdStringStartTime = "2007-09-03T12:00";
        LocalDateTime subtaskThdStartTime = LocalDateTime.parse(subtaskThdStringStartTime);
        Duration subTaskThdDuration = Duration.ofMinutes(subTaskFstDurationInMinutes);
        SubTask subTaskThd = new SubTask(
                (short) 4,
                "Subtask_3",
                "Description_4",
                "NEW",
                epicId,
                subtaskThdStartTime,
                subTaskThdDuration
        );

        taskManager.createEpic(epic);
        List.of(subTaskFst, subTaskSnd, subTaskThd)
                .stream()
                .forEach(subTask -> taskManager.createSubTask(subTask));

        boolean condition1 = epic.startTime.equals(subTaskFst.startTime);
        boolean condition2 = epic.duration.equals(subTaskFst.duration.plus(subTaskThd.duration));
        boolean condition3 = epic.endTime.equals(subTaskThd.getEndTime());

        Assertions.assertTrue(condition1 && condition2 && condition3);
    }

    @Test
    public void epicStatusShouldFormCorrectly() {
        boolean condition1 = epicHasStatusIfSubTasksHaveStatuses(
                "NEW",
                "NEW",
                "NEW"
        );
        boolean condition2 = epicHasStatusIfSubTasksHaveStatuses(
                "DONE",
                "DONE",
                "DONE"
        );
        boolean condition3 = epicHasStatusIfSubTasksHaveStatuses(
                "IN_PROGRESS",
                "NEW",
                "DONE"
        );
        boolean condition4 = epicHasStatusIfSubTasksHaveStatuses(
                "IN_PROGRESS",
                "IN_PROGRESS",
                "IN_PROGRESS"
        );

        boolean condition5 = epicHasStatusIfSubTasksHaveStatuses(
                "IN_PROGRESS",
                "DONE",
                "IN_PROGRESS"
        );

        Assertions.assertTrue(condition1 && condition2 && condition3 && condition4 && condition5);
    }

    public boolean epicHasStatusIfSubTasksHaveStatuses(String epicStatus, String statusOfSubTask1, String statusOfSubTask2) {
        Epic epic = new Epic("Epic_1", "Description of Epic_1", "NEW");
        SubTask subTask1 = new SubTask(
                "SubTask_1",
                "Description of SubTask_1",
                statusOfSubTask1,
                epic.id
        );
        SubTask subTask2 = new SubTask(
                "SubTask_2",
                "Description of SubTask_2",
                statusOfSubTask2,
                epic.id
        );

        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);

        return taskManager.getEpic(epic.id).status == Statuses.valueOf(epicStatus);
    }

    @Test
    public void shouldReturnPrioritizedTasksListCorrectly() {
        LocalDateTime startTime1 = LocalDateTime.of(2024, 6, 3, 12, 0);
        Duration duration = Duration.ofMinutes(30);
        Task task1 = new Task("Task1", "Description1", "NEW", startTime1, duration);

        LocalDateTime startTime2 = startTime1.plusDays(1);
        Task task2 = new Task("Task2", "Description2", "NEW", startTime2, duration);

        Epic epic = new Epic("Epic1", "Description3", "NEW");

        LocalDateTime startTime3 = startTime2.plusDays(1);
        SubTask subTask = new SubTask(
                "SubTask1",
                "Description4",
                "NEW",
                epic.id,
                startTime3,
                duration
        );

        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        TreeSet<Task> prioritizedTasks = (TreeSet<Task>) taskManager.getPrioritizedTasks();
        boolean condition1 = prioritizedTasks.pollFirst() == task1;
        boolean condition2 = prioritizedTasks.pollFirst() == task2;
        boolean condition3 = prioritizedTasks.pollFirst() == subTask;

        Assertions.assertTrue(condition1 && condition2 && condition3);


    }

}
