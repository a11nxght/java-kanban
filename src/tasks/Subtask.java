package tasks;

public class Subtask extends Task{
    final int epicId;

    public Subtask(String name, String description, int taskId, int epicId) {
        super(name, description, taskId);
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
