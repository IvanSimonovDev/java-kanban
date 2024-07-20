package lib.tasks;

import java.util.Comparator;

public class StartDateComparator implements Comparator<Task> {
    public int compare(Task task1, Task task2) {
        int checkEqualOrAfter = task1.startTime.equals(task2.startTime) ? 0 : 1;
        return task1.startTime.isBefore(task2.startTime) ? -1 : checkEqualOrAfter;
    }
}
