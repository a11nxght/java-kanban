package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtasks = new ArrayList<>();
    final Type type = Type.EPIC;

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
        if (subtaskId != taskId && !subtasks.contains(subtaskId)) {
            subtasks.add(subtaskId);
        }
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }

    @Override
    public String toString() {
        return String.format("%d, %s, %s, %s, %s,", taskId, type, name, status, description);
    }
}
