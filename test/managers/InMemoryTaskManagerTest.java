package managers;

import lib.managers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    @BeforeEach
    public void taskManagerCreation() {
        taskManager = new InMemoryTaskManager();
    }
}