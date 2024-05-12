package tasks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EpicTest {
    String title;
    String description;
    String status;

    @BeforeEach
    public void settingParametersForEpic() {
        title = "Epic_1";
        description = "Description_1";
        status = "NEW";
    }

    @Test
    public void constructorWithoutIdShouldWork() {
        Epic epic = new Epic(title, description, status);

        boolean condition = (epic.id > 0) && (epic.title.equals(title)) && (epic.description.equals(description)) &&
                (epic.status.toString().equals(status)) && (epic.subtasksIds.isEmpty());

        Assertions.assertTrue(condition);
    }

    @Test
    public void constructorWithIdShouldWork() {
        short id = 100;
        Epic epic = new Epic(id, title, description, status);

        boolean condition = (epic.id == id) && (epic.title.equals(title)) && (epic.description.equals(description)) &&
                (epic.status.toString().equals(status)) && (epic.subtasksIds.isEmpty());

        Assertions.assertTrue(condition);
    }

    @Test
    public void epicsShouldBeEqualWhenIdsAreEqual() {
        short id = 1;
        Epic epicFst = new Epic(id, "Epic_1", "Description_1", "NEW");
        Epic epicSnd = new Epic(id, "Epic_2", "Description_2", "NEW");
        Assertions.assertEquals(epicFst, epicSnd);
    }

    @Test
    public void hashCodeShouldBeEqualToId() {
        short id = 100;
        Epic epic = new Epic(id, title, description, status);

        Assertions.assertEquals(epic.id, epic.hashCode());
    }

    @Test
    public void methodCloneShouldReturnNotSameObjectWithSameClass() {
        Epic epic = new Epic(title, description, status);
        Epic cloneEpic = epic.clone();

        boolean condition = (epic != cloneEpic) && (epic.getClass() == cloneEpic.getClass());

        Assertions.assertTrue(condition);
    }


}