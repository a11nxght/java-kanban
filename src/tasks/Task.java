package tasks;

import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected int taskId;
    protected Status status;

    public Type getType() {
        return type;
    }

    private final Type type = Type.TASK;
    public Task(String name, String description, int taskId) {
        this.name = name;
        this.description = description;
        this.taskId = taskId;
    }

    public Task(String name, String description, int taskId, Status status) {
        this.taskId = taskId;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Task task = (Task) object;
        return taskId == task.taskId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(taskId);
    }

    @Override
    public String toString() {
        return String.format("%d, %s, %s, %s, %s,", taskId, type, name, status, description);
    }
}
