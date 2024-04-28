package tasks;

import java.util.ArrayList;
import java.util.Random;

public class Task {
    private final static int INITIAL_CAPACITY = 20;
    private final static Random RANDOM_GENERATOR = new Random();
    private static ArrayList<Short> idList = new ArrayList<>(INITIAL_CAPACITY);
    public  short id;
    public String title;
    public String description;
    public Statuses status;


    public Task(String title, String description, String status) {
        this.title = title;
        this.description = description;
        this.status = Statuses.valueOf(status);

        short id = (short) RANDOM_GENERATOR.nextInt(Short.MAX_VALUE);
        while (idList.contains(id)) {
            id = (short) RANDOM_GENERATOR.nextInt();
        }
        this.id = id;
        this.idList.add(id);
    }

    public Task(short id, String title, String description, String status) {
        this.title = title;
        this.description = description;
        this.status = Statuses.valueOf(status);

        this.id = id;
    }


    @Override
    public String toString() {
        String klass = this.getClass().toString().split("\\.")[1];
        return title + " ( " + klass + ", id = " + id + " )";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if ( (o == null) || (this.getClass() != o.getClass()) ) {
            return false;
        }
        Task task = (Task) o;

        if (this.id == task.id) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public int hashCode() {
        return id;
    }
}

enum Statuses {
    NEW,
    IN_PROGRESS,
    DONE
}

