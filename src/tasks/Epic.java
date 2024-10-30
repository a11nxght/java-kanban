package tasks;

import java.util.ArrayList;

public class Epic extends Task{
    private final ArrayList<Integer> subtasks = new ArrayList<>();

    public Epic(String name, String description, int taskId) {
        super(name, description, taskId);
    }

    public Epic(String name, String description) {
        super(name, description);
    }

    public void deleteSubtask(Integer subtaskId) {
        subtasks.remove(subtaskId);
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    public void addSubtask(Integer subtaskId) {
        if (subtaskId != taskId){
            subtasks.add(subtaskId);
        }
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
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
