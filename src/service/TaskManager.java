package service;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getHistory();

    //task
    int createNewTask(Task task);

    void updateTask(Task task);

    List<Task> getAllTasks();

    void deleteAllTasks();

    void deleteTask(int taskId);

    Task getTask(int taskId);

    //epic
    List<Subtask> getSubtasksFromEpic(int epicTaskId);

    int createNewEpic(Epic epic);

    void updateEpic(Epic epic);

    List<Epic> getAllEpics();

    void deleteAllEpics();

    void deleteEpic(int taskId);

    Epic getEpic(int taskId);

    //subtask
    int createNewSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    List<Subtask> getAllSubtasks();

    void deleteAllSubtasks();

    void deleteSubtask(int taskId);

    Subtask getSubtask(int taskId);
}
