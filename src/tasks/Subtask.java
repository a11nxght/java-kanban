package tasks;

public class Subtask extends Task {
    private final int epicId;


    public Subtask(String name, String description, int taskId, int epicId) {
        super(name, description, taskId);
        super.setType(Type.SUBTASK);
        if (taskId != epicId) {
            this.epicId = epicId;
        } else {
            this.epicId = -1;
        }
    }

    public Subtask(String name, String description, int taskId, Status status, int epicId) {
        super(name, description, taskId, status);
        super.setType(Type.SUBTASK);
        if (taskId != epicId) {
            this.epicId = epicId;
        } else {
            this.epicId = -1;
        }
    }

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
        super.setType(Type.SUBTASK);
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d", taskId, type, name, status, description, epicId);
    }

}
