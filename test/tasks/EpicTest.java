package tasks;

import lib.tasks.Epic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EpicTest {
    private String epicTitle;
    private String epicDescription;
    private String epicStatus;

    @BeforeEach
    public void settingParametersForEpic() {
        epicTitle = "Epic_1";
        epicDescription = "Description_1";
        epicStatus = "NEW";
    }

    @Test
    public void constructorWithoutIdShouldWork() {
        Epic epic = new Epic(epicTitle, epicDescription, epicStatus);

        boolean condition = (epic.id > 0) && (epic.title.equals(epicTitle)) &&
                (epic.description.equals(epicDescription)) &&
                (epic.status.toString().equals(epicStatus)) &&
                (epic.subtasksIds.isEmpty());

        Assertions.assertTrue(condition);
    }

    @Test
    public void constructorWithIdShouldWork() {
        short id = 100;
        Epic epic = new Epic(id, epicTitle, epicDescription, epicStatus);

        boolean condition = (epic.id == id) && (epic.title.equals(epicTitle)) &&
                (epic.description.equals(epicDescription)) &&
                (epic.status.toString().equals(epicStatus)) &&
                (epic.subtasksIds.isEmpty());

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
        Epic epic = new Epic(id, epicTitle, epicDescription, epicStatus);

        Assertions.assertEquals(epic.id, epic.hashCode());
    }

    @Test
    public void methodCloneShouldReturnNotSameObjectWithSameClass() {
        Epic epic = new Epic(epicTitle, epicDescription, epicStatus);
        Epic cloneEpic = epic.clone();

        boolean condition = (epic != cloneEpic) && (epic.getClass() == cloneEpic.getClass());

        Assertions.assertTrue(condition);
    }
}
