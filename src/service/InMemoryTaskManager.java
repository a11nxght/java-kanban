package service;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InMemoryTaskManager implements TaskManager {
    private int taskId = 0;
    private final HashMap<Integer, Task> taskTasks = new HashMap<>();
    private final HashMap<Integer, Epic> epicTasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskTasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void deleteAllTasksFromHistoryManager(HashMap<Integer, ? extends Task> tasks) {
        for (Integer i : tasks.keySet()) {
            historyManager.remove(i);
        }
    }

    //task
    @Override
    public int createNewTask(Task task) {
        task.setTaskId(++taskId);
        taskTasks.put(taskId, task);
        return taskId;
    }

    @Override
    public void updateTask(Task task) {
        if (taskTasks.containsValue(task)) {
            int taskId = task.getTaskId();
            taskTasks.replace(taskId, task);
        } else {
            System.out.println("Такой задачи нет.");
        }
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(taskTasks.values());
    }

    @Override
    public void deleteAllTasks() {
        deleteAllTasksFromHistoryManager(taskTasks);
        taskTasks.clear();
    }

    @Override
    public void deleteTask(int taskId) {
        historyManager.remove(taskId);
        taskTasks.remove(taskId);
    }

    @Override
    public Task getTask(int taskId) {
        if (taskTasks.containsKey(taskId)) {
            Task task = taskTasks.get(taskId);
            Task taskForHistory = new Task(task.getName(), task.getDescription(), task.getTaskId());
            taskForHistory.setStatus(task.getStatus());
            historyManager.add(taskForHistory);
            return taskTasks.get(taskId);
        }
        return null;
    }

    //epic
    @Override
    public ArrayList<Subtask> getSubtasksFromEpic(int epicTaskId) {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        if (epicTasks.containsKey(epicTaskId)) {
            Epic epic = epicTasks.get(epicTaskId);
            for (Integer subtaskId : epic.getSubtasks()) {
                subtasks.add(subtaskTasks.get(subtaskId));
            }
        }
        return subtasks;
    }

    @Override
    public int createNewEpic(Epic epic) {
        epic.setTaskId(++taskId);
        epicTasks.put(taskId, epic);
        return taskId;
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epicTasks.containsValue(epic)) {
            int taskId = epic.getTaskId();
            epicTasks.get(taskId).setName(epic.getName());
            epicTasks.get(taskId).setDescription(epic.getDescription());
        } else {
            System.out.println("Нет задачи с таким Id.");
        }
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epicTasks.values());
    }

    @Override
    public void deleteAllEpics() {
        deleteAllTasksFromHistoryManager(subtaskTasks);
        subtaskTasks.clear();
        deleteAllTasksFromHistoryManager(epicTasks);
        epicTasks.clear();
    }

    @Override
    public void deleteEpic(int taskId) {
        if (epicTasks.containsKey(taskId)) {
            Epic epic = epicTasks.get(taskId);
            for (Integer subtaskId : epic.getSubtasks()) {
                subtaskTasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
            historyManager.remove(taskId);
            epicTasks.remove(taskId);
        } else System.out.println("Нет эпика с таким Id");
    }

    @Override
    public Epic getEpic(int taskId) {
        if (epicTasks.containsKey(taskId)) {
            Epic epic = epicTasks.get(taskId);
            Epic epicForHistory = new Epic(epic.getName(), epic.getDescription(), epic.getTaskId());
            for (int subtaskId : epic.getSubtasks()) {
                epicForHistory.addSubtask(subtaskId);
            }
            epicForHistory.setStatus(epic.getStatus());
            historyManager.add(epicForHistory);
            return epicTasks.get(taskId);
        }
        return null;
    }

    //subtask
    @Override
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

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtaskTasks.containsValue(subtask)) {
            subtaskTasks.replace(subtask.getTaskId(), subtask);
            Epic epic = epicTasks.get(subtask.getEpicId());
            updateEpicStatus(epic);
        } else {
            System.out.println("Такой подзадачи нет.");
        }
    }

    private void updateEpicStatus(Epic epic) {
        if (epic.getSubtasks().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        int doneSubtasks = 0;
        int newSubtasks = 0;
        for (Integer subtaskId : epic.getSubtasks()) {
            if (subtaskTasks.get(subtaskId).getStatus() == Status.IN_PROGRESS) {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            } else if (subtaskTasks.get(subtaskId).getStatus() == Status.DONE) {
                doneSubtasks++;
            } else if (subtaskTasks.get(subtaskId).getStatus() == Status.NEW) {
                newSubtasks++;
            }
        }
        if (doneSubtasks == epic.getSubtasks().size()) {
            epic.setStatus(Status.DONE);
        } else if (newSubtasks == epic.getSubtasks().size()) {
            epic.setStatus(Status.NEW);
        }
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtaskTasks.values());
    }

    @Override
    public void deleteAllSubtasks() {
        for (Epic epic : epicTasks.values()) {
            epic.deleteAllSubtasks();
            updateEpicStatus(epic);
        }
        deleteAllTasksFromHistoryManager(subtaskTasks);
        subtaskTasks.clear();
    }

    @Override
    public void deleteSubtask(int taskId) {
        if (subtaskTasks.containsKey(taskId)) {
            Subtask subtask = subtaskTasks.get(taskId);
            Epic epic = epicTasks.get(subtask.getEpicId());
            epic.deleteSubtask(taskId);
            updateEpicStatus(epic);
            historyManager.remove(taskId);
            subtaskTasks.remove(taskId);
        } else System.out.println("Подзадачи с таким Id нет.");
    }

    @Override
    public Subtask getSubtask(int taskId) {
        if (subtaskTasks.containsKey(taskId)) {
            Subtask subtask = subtaskTasks.get(taskId);
            Subtask subtaskForHistory = new Subtask(subtask.getName(), subtask.getDescription(), subtask.getTaskId(), subtask.getEpicId());
            subtaskForHistory.setStatus(subtask.getStatus());
            historyManager.add(subtaskForHistory);
            return subtaskTasks.get(taskId);
        }
        return null;
    }
}
