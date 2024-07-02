package lib.tasks;

import java.util.Comparator;

public class StartDateComparator implements Comparator<Task> {
    public int compare(Task task1, Task task2) {
        return task1.startTime.isBefore(task2.startTime) ? -1 : 1;
    }
}
