package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<SubTask> subTasksList;

    public Epic(String title, String description, ArrayList<SubTask> subTasksList) {
        super(title, description, "NEW");
        this.subTasksList = subTasksList;
    }

    public Epic(String title, String description, int taskId, ArrayList<SubTask> subTasksList) {
        this(title, description, subTasksList);
        this.taskId = taskId;
    }

    public boolean areAllSubTasksInStatus(String status) {
        Statuses enumStatus = Statuses.valueOf(status);
        boolean result = true;
        for ( SubTask subTask : subTasksList) {
            result = result && ( enumStatus == subTask.status);
        }
        return result;
    }




}
