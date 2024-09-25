package service;

import tasks.Epic;
import tasks.Status;
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
            for (Integer subtaskId : epic.getSubtasks()) {
                subtasks.add(subtaskTasks.get(subtaskId));
            }
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
            for (Integer subtaskId : epic.getSubtasks()) {
                subtaskTasks.remove(subtaskId);
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
            epic.addSubtask(taskId);
            return taskId;
        }
        System.out.println("Нет эпика с таким Id");
        return -1;
    }

    public void updateSubtaskTask(Subtask subtask) {
        if (subtaskTasks.containsValue(subtask)) {
            subtaskTasks.replace(subtask.getTaskId(), subtask);
            Epic epic = epicTasks.get(subtask.getEpicId());
            updateEpicStatus(epic);
        } else {
            System.out.println("Такой подзадачи нет.");
        }
    }

    private void updateEpicStatus(Epic epic) {
        if (epic.getSubtasks().isEmpty()){
            epic.setStatus(Status.NEW);
            return;
        }
        int doneSubtasks = 0;
        int newSubtasks = 0;
        for (Integer subtaskId : epic.getSubtasks()){
            if (subtaskTasks.get(subtaskId).getStatus() == Status.IN_PROGRESS) {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            } else if (subtaskTasks.get(subtaskId).getStatus() == Status.DONE) {
                doneSubtasks++;
            } else if (subtaskTasks.get(subtaskId).getStatus() == Status.NEW) {
                newSubtasks++;
            }
        }
        if (doneSubtasks == epic.getSubtasks().size()){
            epic.setStatus(Status.DONE);
        } else if (newSubtasks == epic.getSubtasks().size()) {
            epic.setStatus(Status.NEW);
        }
    }

    public ArrayList<Subtask> getAllSubtaskTusks() {
        return new ArrayList<>(subtaskTasks.values());
    }

    public void deleteSubtaskTasks() {
        for (Epic epic : epicTasks.values()) {
            epic.deleteAllSubtasks();
            updateEpicStatus(epic);
        }
        subtaskTasks.clear();
    }

    public void deleteSubtaskTaskById(int taskId) {
        if (subtaskTasks.containsKey(taskId)){
            Subtask subtask = subtaskTasks.get(taskId);
            Epic epic = epicTasks.get(subtask.getEpicId());
            epic.deleteSubtask(taskId);
            updateEpicStatus(epic);
            subtaskTasks.remove(taskId);
        } else System.out.println("Подзадачи с таким Id нет.");
    }

    public Subtask getSubtaskTaskById(int taskId) {
        return subtaskTasks.get(taskId);
    }
}
