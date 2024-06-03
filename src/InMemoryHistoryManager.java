import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final DoublyLinkedList<Short, Task> historyStorage;

    public InMemoryHistoryManager() {
        historyStorage = new DoublyLinkedList<>();
    }

    @Override
    public List<Task> getHistory() {
        DoublyLinkedList<Short, Task>.Node<Task> currentNode = historyStorage.getHead();
        ArrayList<Task> result = new ArrayList<>(historyStorage.size());

        while (currentNode != null) {
            result.add(currentNode.getValue());
            currentNode = currentNode.getNextNode();
        }
        return result;
    }

    @Override
    public void add(Task watchedTask) {
        if (watchedTask != null) {
            historyStorage.addFirst(watchedTask.id, watchedTask.clone());
        }
    }

    @Override
    public void remove(short id) {
        historyStorage.remove(id);
    }
}
