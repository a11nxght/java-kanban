package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected int taskId;
    protected Status status;

    protected Duration duration;

    protected LocalDateTime startTime;


    protected final Type type;

    public LocalDateTime getEndTime(){
        if (startTime != null){
            return startTime.plus(duration);
        }
        return null;
    }

    public Type getType() {
        return type;
    }

    public Task(Type type, String name, String description, int taskId) {
        this.name = name;
        this.description = description;
        this.taskId = taskId;
        this.type = type;
        this.duration = Duration.ofMinutes(0);
    }

    public Task(Type type, String name, String description, int taskId, Status status) {
        this.taskId = taskId;
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = type;
        this.duration = Duration.ofMinutes(0);
    }

    public Task(Type type, String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.type = type;
        this.duration = Duration.ofMinutes(0);
    }

    public Task(Type type, String name, String description, Duration duration, LocalDateTime startTime) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
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
        String result = String.format("%d,%s,%s,%s,%s,,%d,", taskId, type, name, status, description, duration.toSeconds());
        if (startTime != null) {
            result += startTime.toEpochSecond(ZoneOffset.ofHours(3)) ;
        }
        return result;
    }
}
