package tasks;

public class Subtask extends Task{
    private final int epicId;

    public Subtask(String name, String description, int taskId, int epicId) {
        super(name, description, taskId);
        if (taskId != epicId) {
            this.epicId = epicId;
        }
        else {
            this.epicId = -1;
        }
    }

    public Subtask(String name, String description, int taskId, Status status, int epicId) {
        super(name, description, taskId, status);
        if (taskId != epicId) {
            this.epicId = epicId;
        }
        else {
            this.epicId = -1;
        }
    }

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", taskId=" + taskId +
                ", status=" + status +
                '}';
    }
}
