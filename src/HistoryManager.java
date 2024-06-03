import tasks.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task);

    void remove(short id);

    List<Task> getHistory();
}
