package lib.managers;

public class Managers {
    public static TaskManager getDefaultInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }
    public static TaskManager getDefaultFileBackedTaskManager() { return new FileBackedTaskManager(); }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
