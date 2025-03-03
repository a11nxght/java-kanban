package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtasks = new ArrayList<>();
    private LocalDateTime endTime;

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Epic(Type type, String name, String description, int taskId) {
        super(type, name, description, taskId);
    }

    public Epic(Type type, String name, String description) {
        super(type, name, description);
    }

    public void deleteSubtask(Integer subtaskId) {
        subtasks.remove(subtaskId);
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    public void addSubtask(Integer subtaskId) {
        if (subtaskId != taskId && !subtasks.contains(subtaskId)) {
            subtasks.add(subtaskId);
        }
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }

}
