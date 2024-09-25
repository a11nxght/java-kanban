package tasks;

import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task{
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public Epic(String name, String description, int taskId) {
        super(name, description, taskId);
    }

    public Epic(String name, String description) {
        super(name, description);
    }

    public void deleteSubtask(Subtask subtask) {
        subtasks.remove(subtask.getTaskId());
        updateStatus();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
        updateStatus();
    }

    public void addSubtask(Subtask subtask) {
        subtasks.put(subtask.getTaskId(), subtask);
        updateStatus();
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.replace(subtask.getTaskId(), subtask);
        updateStatus();
    }

    public void updateStatus() {
        if (subtasks.isEmpty()){
            status = Status.NEW;
            return;
        }
        int doneSubtasks = 0;
        int newSubtasks = 0;
        for (Subtask iSubtask : subtasks.values()){
            if (iSubtask.getStatus() == Status.IN_PROGRESS) {
                status = Status.IN_PROGRESS;
                return;
            } else if (iSubtask.getStatus() == Status.DONE) {
                doneSubtasks++;
            } else if (iSubtask.getStatus() == Status.NEW) {
                newSubtasks++;
            }
        }
        if (doneSubtasks == subtasks.size()){
            status = Status.DONE;
        } else if (newSubtasks == subtasks.size()) {
            status = Status.NEW;
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasks=" + subtasks +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", taskId=" + taskId +
                ", status=" + status +
                '}';
    }
}
