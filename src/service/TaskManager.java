package service;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int taskId = 0;
    private final HashMap<Integer, Task> taskTasks = new HashMap<>();
    private final HashMap<Integer, Epic> epicTasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskTasks = new HashMap<>();

    //task
    public int createNewTask(Task task) {
        task.setTaskId(++taskId);
        taskTasks.put(taskId, task);
        return taskId;
    }

    public void updateTaskTask(Task task) {
        if (taskTasks.containsValue(task)) {
            int taskId = task.getTaskId();
            taskTasks.replace(taskId, task);
        } else {
            System.out.println("Такой задачи нет.");
        }
    }

    public ArrayList<Task> getAllTaskTasks() {
        return new ArrayList<>(taskTasks.values());
    }

    public void deleteTaskTasks() {
        taskTasks.clear();
    }

    public void deleteTaskTaskById(int taskId) {
        taskTasks.remove(taskId);
    }

    public Task getTaskTaskById(int taskId) {
        return taskTasks.get(taskId);
    }

    //epic
    public ArrayList<Subtask> getSubtaskTasksFromEpic(int epicTaskId) {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        if (epicTasks.containsKey(epicTaskId)) {
            Epic epic = epicTasks.get(epicTaskId);
            subtasks = epic.getSubtasks();
        }
        return subtasks;
    }

    public int createNewEpic(Epic epic) {
        epic.setTaskId(++taskId);
        epicTasks.put(taskId, epic);
        return taskId;
    }

    public void updateEpicTask(Epic epic) {
        if (epicTasks.containsValue(epic)) {
            int taskId = epic.getTaskId();
            epicTasks.get(taskId).setName(epic.getName());
            epicTasks.get(taskId).setDescription(epic.getDescription());
        } else {
            System.out.println("Нет задачи с таким Id.");
        }
    }

    public ArrayList<Epic> getAllEpicTasks() {
        return new ArrayList<>(epicTasks.values());
    }

    public void deleteEpicTasks() {
        subtaskTasks.clear();
        epicTasks.clear();
    }

    public void deleteEpicTaskById(int taskId) {
        if (epicTasks.containsKey(taskId)){
            Epic epic = epicTasks.get(taskId);
            for (Subtask subtask : epic.getSubtasks()) {
                subtaskTasks.remove(subtask.getTaskId());
            }
            epicTasks.remove(taskId);
        } else System.out.println("Нет эпика с таким Id");
    }

    public Epic getEpicTaskById(int taskId) {
        return epicTasks.get(taskId);
    }

    //subtask
    public int createNewSubtask(Subtask subtask) {
        if (epicTasks.containsKey(subtask.getEpicId())) {
            subtask.setTaskId(++taskId);
            subtaskTasks.put(taskId, subtask);
            Epic epic = epicTasks.get(subtask.getEpicId());
            epic.addSubtask(subtask);
            return taskId;
        }
        System.out.println("Нет эпика с таким Id");
        return -1;
    }

    public void updateSubtaskTask(Subtask subtask) {
        if (subtaskTasks.containsValue(subtask)) {
            subtaskTasks.replace(subtask.getTaskId(), subtask);
            Epic epic = epicTasks.get(subtask.getEpicId());
            epic.updateSubtask(subtask);
        } else {
            System.out.println("Такой подзадачи нет.");
        }
    }

    public ArrayList<Subtask> getAllSubtaskTusks() {
        return new ArrayList<>(subtaskTasks.values());
    }

    public void deleteSubtaskTasks() {
        for (Epic epic : epicTasks.values()) {
            epic.deleteAllSubtasks();
        }
        subtaskTasks.clear();
    }

    public void deleteSubtaskTaskById(int taskId) {
        if (subtaskTasks.containsKey(taskId)){
            Subtask subtask = subtaskTasks.get(taskId);
            Epic epic = epicTasks.get(subtask.getEpicId());
            epic.deleteSubtask(subtask);
            subtaskTasks.remove(taskId);
        } else System.out.println("Подзадачи с таким Id нет.");
    }

    public Subtask getSubtaskTaskById(int taskId) {
        return subtaskTasks.get(taskId);
    }
}
