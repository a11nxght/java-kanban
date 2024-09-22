package tasks;

import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task{
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public Epic(String name, String description, int taskId) {
        super(name, description, taskId);
    }

    public void deleteSubtask(Subtask subtask) {
        subtasks.remove(subtask.getTaskId());
    }

    public void addSubtask(Subtask subtask) {
        subtasks.put(subtask.getTaskId(), subtask);
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void updateSubtask(Subtask subtask) {
        int taskId = subtask.getTaskId();
        subtasks.replace(subtask.getTaskId(), subtask);
        int done = 0;
        for (Subtask iSubtask : subtasks.values()){
            if (iSubtask.getStatus() == Status.IN_PROGRESS) {
                super.setStatus(Status.IN_PROGRESS);
                break;
            } else if (iSubtask.getStatus() == Status.DONE) {
                done++;
            }
        }
        if (done == subtasks.size()) super.setStatus(Status.DONE);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasks=" + subtasks +
                ", name='" + super.getName() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", taskId=" + super.getTaskId() +
                ", status=" + super.getStatus() +
                '}';
    }
}
