import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final DoublyLinkedList<Task> historyStorage;

    public InMemoryHistoryManager() {
        historyStorage = new DoublyLinkedList<>();
    }

    @Override
    public List<Task> getHistory() {
        DoublyLinkedList<Task>.Node<Task> currentNode = historyStorage.getHead();
        List<Task> result = new ArrayList<>(historyStorage.size());

        while (currentNode != null) {
            result.add(currentNode.getValue());
            currentNode = currentNode.getNextNode();
        }
        return result;
    }

    @Override
    public void add(Task watchedTask) {
        if (watchedTask != null) {
            historyStorage.addFirst(watchedTask.clone());
        }
    }

    @Override
    public void remove(short id) {
        historyStorage.remove(id);
    }
}
