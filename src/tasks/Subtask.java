package tasks;

public class Subtask extends Task{
    private final int epicId;

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
                ", name='" + super.getName() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", taskId=" + super.getTaskId() +
                ", status=" + super.getStatus() +
                '}';
    }
}
