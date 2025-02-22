package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Subtask extends Task {
    private final int epicId;


    public Subtask(Type type, String name, String description, int taskId, int epicId) {
        super(type, name, description, taskId);
        if (taskId != epicId) {
            this.epicId = epicId;
        } else {
            this.epicId = -1;
        }
    }

    public Subtask(Type type, String name, String description, int taskId, Status status, int epicId) {
        super(type, name, description, taskId, status);
        if (taskId != epicId) {
            this.epicId = epicId;
        } else {
            this.epicId = -1;
        }
    }

    public Subtask(Type type, String name, String description, int epicId) {
        super(type, name, description);
        this.epicId = epicId;
    }

    public Subtask(Type type, String name, String description, Duration duration, LocalDateTime startTime, int epicId) {
        super(type, name, description, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        String result =  String.format("%d,%s,%s,%s,%s,%d,%d,", taskId, type, name, status, description, epicId, duration.toSeconds());
        if (startTime != null) {
            result += startTime.toEpochSecond(ZoneOffset.ofHours(3)) ;
        }
        return result;
    }

}
