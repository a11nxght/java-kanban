package tasks;

import java.util.ArrayList;

public class Epic extends Task{
    ArrayList<Subtask> subtasks = new ArrayList<>();

    public Epic(String name, String description, int taskId) {
        super(name, description, taskId);
    }

    public void deleteSubtask(Subtask subtask) {
        subtasks.remove(subtask);
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public ArrayList<Subtask> getSubtasks() {
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
