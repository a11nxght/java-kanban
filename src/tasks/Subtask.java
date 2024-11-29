package tasks;

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

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d", taskId, type, name, status, description, epicId);
    }

}
