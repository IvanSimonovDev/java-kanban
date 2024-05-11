import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    final List<Task> historyStorage;
    final static int MAX_ELEMENTS_IN_HISTORY = 10;

    public InMemoryHistoryManager() {
        historyStorage = new ArrayList<>();
    }

    @Override
    public List<Task> getHistory() {
        return historyStorage;
    }

    @Override
    public void add(Task watchedTask) {
        historyStorage.add(watchedTask);
        if (historyStorage.size() > MAX_ELEMENTS_IN_HISTORY) {
            historyStorage.removeFirst();
        }
    }
}
