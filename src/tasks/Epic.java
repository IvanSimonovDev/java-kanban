package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<SubTask> subTasksList;

    public Epic(String title, String description) {
        super(title, description);
        this.subTasksList = new ArrayList<>();
    }

    public void ultimateSetter(String title, String description, String status, ArrayList<SubTask> subTasksList) {
        super.ultimateSetter(title, description, status);
        this.subTasksList = subTasksList;

        for(SubTask subTask : subTasksList) {
            subTask.setStatus(status);
        }
    }

    @Override
    public void setStatus(String status) {
        this.status = Statuses.valueOf(status);

        for(SubTask subTask : subTasksList) {
            subTask.setStatus(status);
        }
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
