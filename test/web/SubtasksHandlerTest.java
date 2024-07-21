package web;

import lib.tasks.Epic;
import lib.tasks.SubTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class SubtasksHandlerTest extends HandlersTest {
    @Test
    // Testing endpoint GET /subtasks
    public void fifthEndPointShouldWorkCorrectlyWhenThereAreNoSubTasks() throws IOException, InterruptedException {
        List<SubTask> listOfSubTasks = requestForAllSubTasks();

        boolean condition1 = httpResponse.statusCode() == 200;
        boolean condition2 = listOfSubTasks.isEmpty();
        Assertions.assertTrue(condition1 && condition2);
    }

    @Test
    // Testing endpoint Post /subtasks and Post /epics
    public void sixthAndSeventhEndPointsShouldWorkCorrectly() throws IOException, InterruptedException {
        Epic epic = createDefaultEpic();
        requestToCreateOneEpic(epic);
        boolean condition1 = httpResponse.statusCode() == 201;
        boolean condition2 = httpResponse.body().isEmpty();
        boolean condition3 = requestForAllEpics().size() == 1;

        short epicId = requestForAllEpics().getFirst().id;
        SubTask subTask = createDefaultSubTask(epicId);
        requestToCreateOneSubTask(subTask);
        boolean condition4 = httpResponse.statusCode() == 201;
        boolean condition5 = httpResponse.body().isEmpty();

        boolean condition6 = requestForAllSubTasks().getFirst().title.equals(subTask.title);
        boolean condition7 = requestForAllSubTasks().size() == 1;
        boolean condition8 = requestForAllEpics().getFirst().title.equals(epic.title);
        boolean condition9 = requestForAllEpics().size() == 1;

        boolean result = condition1 &&
                condition2 &&
                condition3 &&
                condition4 &&
                condition5 &&
                condition6 &&
                condition7 &&
                condition8 &&
                condition9;
        Assertions.assertTrue(result);
    }

    @Test
    // Testing endpoint DELETE /subtasks/:id
    public void eighthEndPointShouldWorkCorrectly() throws IOException, InterruptedException {
        Epic epic = createDefaultEpic();
        requestToCreateOneEpic(epic);

        short epicId = requestForAllEpics().getFirst().id;
        SubTask subTask = createDefaultSubTask(epicId);
        requestToCreateOneSubTask(subTask);

        short idForRequest = requestForAllSubTasks().getFirst().id;
        requestToDeleteOneSubTask(idForRequest);

        Assertions.assertTrue(requestForAllSubTasks().isEmpty());
    }

    // Testing endpoint GET /subtasks/:id
    @Test
    public void ninthEndPointShouldWorkCorrectly() throws IOException, InterruptedException {
        Epic epic = createDefaultEpic();
        requestToCreateOneEpic(epic);

        short epicId = requestForAllEpics().getFirst().id;
        SubTask subTask = createDefaultSubTask(epicId);
        requestToCreateOneSubTask(subTask);

        short idForRequest = requestForAllSubTasks().getFirst().id;
        SubTask returnedSubTask = requestForOneSubTask(idForRequest);

        Assertions.assertEquals(subTask.title, returnedSubTask.title);
    }

    public static Epic createDefaultEpic() {
        return new Epic((short) 2, "Epic1", "Description3", "NEW");
    }

    public static SubTask createDefaultSubTask(short epicId) {
        return new SubTask(
                (short) 3,
                "SubTask1",
                "Description4",
                "NEW",
                epicId,
                LocalDateTime.parse("2024-11-12T13:00"),
                Duration.ofMinutes(33)
        );
    }

    public static SubTask createAnotherDefaultSubTask(short epicId) {
        return new SubTask(
                (short) 4,
                "SubTask2",
                "Description5",
                "NEW",
                epicId,
                LocalDateTime.parse("2024-11-13T13:00"),
                Duration.ofMinutes(33)
        );
    }
}
