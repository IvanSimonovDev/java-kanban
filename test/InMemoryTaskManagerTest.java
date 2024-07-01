import lib.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import lib.tasks.Epic;
import lib.tasks.Statuses;
import lib.tasks.SubTask;
import lib.tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class InMemoryTaskManagerTest {
    private static InMemoryTaskManager inMemoryTaskManager;

    @BeforeEach
    public void taskManagerCreation() {
        inMemoryTaskManager = new InMemoryTaskManager();
    }

    @Test
    public void canCreateSubTaskAndFindItById() {

        Epic epicOfSubTask = new Epic("Epic_1", "Description of Epic_1", "NEW");
        SubTask subTask = new SubTask("SubTask_1", "Description of SubTask_1", "NEW", epicOfSubTask.id);

        inMemoryTaskManager.createEpic(epicOfSubTask);
        inMemoryTaskManager.createSubTask(subTask);

        SubTask returnedSubTask = inMemoryTaskManager.getSubTask(subTask.id);

        Assertions.assertEquals(subTask, returnedSubTask);
    }

    @Test
    public void canCreateTaskAndFindItById() {

        Task task = new Task("Task_1", "Description of Task_1", "NEW");

        inMemoryTaskManager.createTask(task);

        Task returnedTask = inMemoryTaskManager.getTask(task.id);

        Assertions.assertEquals(task, returnedTask);
    }

    @Test
    public void canCreateEpicAndFindItById() {

        Epic epic = new Epic("Epic_1", "Description of Epic_1", "NEW");

        inMemoryTaskManager.createEpic(epic);

        Epic returnedEpic = inMemoryTaskManager.getEpic(epic.id);

        Assertions.assertEquals(epic, returnedEpic);
    }

    @Test
    public void TaskWithSetIdAndTaskWithGeneratedIdDoNotConflict() {
        Task task = new Task("Task_1", "Description of Task_1", "NEW");
        inMemoryTaskManager.createTask(task);

        Task updatedTask = new Task(task.id, "Another Task_1", "Description of Task_1", "DONE");
        inMemoryTaskManager.updateTask(updatedTask);

        List<Task> listOfTasks = inMemoryTaskManager.getTasksList();

        boolean condition = (listOfTasks.size() == 1) &&
                inMemoryTaskManager.getTask(updatedTask.id).title.equals("Another Task_1");

        Assertions.assertTrue(condition);
    }

    @Test
    public void TaskDoesNotChangeAfterAddingToManager() {
        Task task = new Task("Task_1", "Description of Task_1", "NEW");
        inMemoryTaskManager.createTask(task);
        Task returnedTask = inMemoryTaskManager.getTask(task.id);
        boolean condition = (task.id == returnedTask.id) && (task.title.equals(returnedTask.title)) &&
                (task.description.equals(returnedTask.description)) && (task.status.equals(returnedTask.status));

        Assertions.assertTrue(condition);
    }

    @Test
    public void epicShouldNotContainDeletedSubTask() {

        Epic epicOfSubTask = new Epic("Epic_1", "Description of Epic_1", "NEW");
        SubTask subTask = new SubTask("SubTask_1", "Description of SubTask_1", "NEW", epicOfSubTask.id);

        inMemoryTaskManager.createEpic(epicOfSubTask);
        inMemoryTaskManager.createSubTask(subTask);
        inMemoryTaskManager.deleteSubTask(subTask.id);

        Assertions.assertTrue(epicOfSubTask.subtasksIds.isEmpty());

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
    public void taskFieldsCanChangeOutsideOfTaskManager() {
        short taskId = 22;
        Task task = new Task(taskId, "Task_1", "Description of Task_1",
                "NEW");

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
        int subTaskThdDurationInMinutes = 12;
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

        inMemoryTaskManager.createEpic(epic);
        List.of(subTaskFst, subTaskSnd, subTaskThd)
                .stream()
                .peek(subTask -> inMemoryTaskManager.createSubTask(subTask))
                .toList();

        boolean condition1 = epic.startTime.equals(subTaskFst.startTime);
        boolean condition2 = epic.duration.equals(subTaskFst.duration.plus(subTaskThd.duration));
        boolean condition3 = epic.endTime.equals(subTaskThd.getEndTime());

        Assertions.assertTrue(condition1 && condition2 && condition3);

    }


}