package tasks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubTaskTest {

    private String title;
    private String description;
    private String status;

    private short epicId;

    @BeforeEach
    public void settingParametersForSubTask() {
        title = "SubTask_1";
        description = "Description_1";
        status = "NEW";
        epicId = 77;
    }


    @Test
    public void constructorWithoutIdShouldWork() {
        SubTask subTask = new SubTask(title, description, status, epicId);

        boolean condition = (subTask.id > 0) && (subTask.title.equals(title)) && (subTask.description.equals(description)) &&
                (subTask.status.toString().equals(status)) && (subTask.epicId == epicId);

        Assertions.assertTrue(condition);
    }

    @Test
    public void constructorWithIdShouldWork() {
        short id = 100;
        SubTask subTask = new SubTask(id, title, description, status, epicId);

        boolean condition = (subTask.id == id) && (subTask.title.equals(title)) && (subTask.description.equals(description)) &&
                (subTask.status.toString().equals(status)) && (subTask.epicId == epicId);

        Assertions.assertTrue(condition);
    }

    @Test
    public void subTasksShouldBeEqualWhenIdsAreEqual() {
        short subTaskId = 1;
        short epicFstId = 4;
        short epicSndId = 9;
        SubTask subTaskFst = new SubTask(subTaskId, "SubTask_1", "Description_1", "NEW", epicFstId);
        SubTask subTaskSnd = new SubTask(subTaskId, "SubTask_2", "Description_2", "NEW", epicSndId);
        Assertions.assertEquals(subTaskFst, subTaskSnd);
    }

    @Test
    public void hashCodeShouldBeEqualToId() {
        short id = 100;
        SubTask subTask = new SubTask(id, title, description, status, epicId);

        Assertions.assertEquals(subTask.id, subTask.hashCode());
    }

    @Test
    public void methodCloneShouldReturnNotSameObjectWithSameClass() {
        SubTask subTask = new SubTask(title, description, status, epicId);
        SubTask cloneSubTask = subTask.clone();

        boolean condition = (subTask != cloneSubTask) && (subTask.getClass() == cloneSubTask.getClass());

        Assertions.assertTrue(condition);
    }

}