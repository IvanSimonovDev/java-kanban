package tasks;

public class SubTask extends Task {
    public final Epic epicObject;

    public SubTask(String title, String description, Epic epicObject) {
        super(title, description);
        this.epicObject = epicObject;

        if (epicObject.status == Statuses.DONE) {
            epicObject.status = Statuses.IN_PROGRESS;
        }

        epicObject.subTasksList.add(this);


    }


    @Override
    public void setStatus(String status) {
        this.status = Statuses.valueOf(status);

        switch (this.status) {
            case NEW:
                if (epicObject.areAllSubTasksInStatus("NEW")) {
                    epicObject.setStatus("NEW");
                } else {
                      epicObject.setStatus("IN_PROGRESS");
                }
                break;
            case IN_PROGRESS:
                epicObject.setStatus("IN_PROGRESS");
                break;
            case DONE:
                if (epicObject.areAllSubTasksInStatus("DONE")) {
                    epicObject.setStatus("DONE");
                } else {
                      epicObject.setStatus("IN_PROGRESS");
                }
                break;
        }
    }

    @Override
    public void ultimateSetter(String title, String description, String status) {
        this.title = title;
        this.description = description;
        this.setStatus(status);
    }






}
