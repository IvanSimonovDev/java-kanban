import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> historyStorage;
    private final static int MAX_ELEMENTS_IN_HISTORY = 10;

    public InMemoryHistoryManager() {
        historyStorage = new ArrayList<>();
    }

    @Override
    public List<Task> getHistory() {
        return historyStorage;
    }

    @Override
    public void add(Task watchedTask) {
        if (watchedTask != null) {
            historyStorage.add(watchedTask.clone());
            if (historyStorage.size() > MAX_ELEMENTS_IN_HISTORY) {
                historyStorage.removeFirst();
            }
        }
    }
}
