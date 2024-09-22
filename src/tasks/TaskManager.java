package tasks;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> taskTasks = new HashMap<>();
    private HashMap<Integer, Epic> epicTasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskTasks = new HashMap<>();

    //task
    public void createNewTask(Task task) {
        taskTasks.put(task.getTaskId(), task);
    }

    public void updateTaskTask(Task task){
        int taskId = task.getTaskId();
        taskTasks.replace(taskId, task);
    }

    public ArrayList<Task> getAllTaskTasks() {
        return new ArrayList<>(taskTasks.values());
    }

    public void deleteTaskTasks() {
        taskTasks.clear();
    }

    public void deleteTaskTaskById (int taskId) {
        taskTasks.remove(taskId);
    }

    public Task getTaskTaskById(int taskId) {
        return taskTasks.get(taskId);
    }

    //epic
    public ArrayList<Subtask> getSubtaskTasksFromEpic(Epic epic) {
        return epic.getSubtasks();
    }

    public void createNewEpic (Epic epic) {
        epicTasks.put(epic.getTaskId(), epic);
    }

    public void updateEpicTask(Epic epic) {
        int taskId = epic.getTaskId();
        epicTasks.replace(taskId, epic);
    }

    public ArrayList<Epic> getAllEpicTasks() {
        return new ArrayList<>(epicTasks.values());
    }

    public void deleteEpicTasks() {
        subtaskTasks.clear();
        epicTasks.clear();
    }

    public void deleteEpicTaskById(int taskId) {
        Epic epic = epicTasks.get(taskId);
        for (Subtask subtask : epic.getSubtasks()) {
            subtaskTasks.remove(subtask.getTaskId());
        }
        epicTasks.remove(taskId);
    }

    public Epic getEpicTaskById(int taskId) {
        return epicTasks.get(taskId);
    }

    //subtask
    public void createNewSubtask(Subtask subtask) {
        subtaskTasks.put(subtask.getTaskId(), subtask);
        Epic epic = epicTasks.get(subtask.getEpicId());
        epic.addSubtask(subtask);
    }

    public void updateSubtaskTask(Subtask subtask) {
        subtaskTasks.replace(subtask.getTaskId(), subtask);
        Epic epic = epicTasks.get(subtask.getEpicId());
        epic.updateSubtask(subtask);
    }

    public ArrayList<Subtask> getAllSubtaskTusks() {
        return new ArrayList<>(subtaskTasks.values());
    }

    public void deleteSubtaskTasks() {
        for (Subtask subtask : subtaskTasks.values()) {
            Epic epic = epicTasks.get(subtask.getEpicId());
            epic.deleteSubtask(subtask);
        }
        subtaskTasks.clear();
    }

    public void deleteSubtaskTaskById(int taskId) {
        Subtask subtask = subtaskTasks.get(taskId);
        Epic epic = epicTasks.get(subtask.getEpicId());
        epic.deleteSubtask(subtask);
        subtaskTasks.remove(taskId);
    }

    public Subtask getSubtaskTaskById(int taskId) {
        return subtaskTasks.get(taskId);
    }
}
