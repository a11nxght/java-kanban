package service;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;

public interface TaskManager {
    //task
    int createNewTask(Task task);

    void updateTaskTask(Task task);

    ArrayList<Task> getAllTaskTasks();

    void deleteTaskTasks();

    void deleteTaskTaskById(int taskId);

    Task getTaskTaskById(int taskId);

    //epic
    ArrayList<Subtask> getSubtaskTasksFromEpic(int epicTaskId);

    int createNewEpic(Epic epic);

    void updateEpicTask(Epic epic);

    ArrayList<Epic> getAllEpicTasks();

    void deleteEpicTasks();

    void deleteEpicTaskById(int taskId);

    Epic getEpicTaskById(int taskId);

    //subtask
    int createNewSubtask(Subtask subtask);

    void updateSubtaskTask(Subtask subtask);

    ArrayList<Subtask> getAllSubtaskTusks();

    void deleteSubtaskTasks();

    void deleteSubtaskTaskById(int taskId);

    Subtask getSubtaskTaskById(int taskId);
}
