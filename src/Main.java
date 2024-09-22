import tasks.*;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("first", "first task", taskManager.taskId++);
        Task task2 = new Task("second", "second task", taskManager.taskId++);
        Epic epic1 = new Epic("third", "first epic", taskManager.taskId++);
        Subtask subtask1 = new Subtask("forth", "first subtask", taskManager.taskId++, epic1.getTaskId());
        Subtask subtask2 = new Subtask("fifth", "second subtask", taskManager.taskId++, epic1.getTaskId());
        Epic epic2 = new Epic("sixth", "second epic", taskManager.taskId++);
        Subtask subtask3 = new Subtask("seventh", "first subtask", taskManager.taskId++, epic2.getTaskId());

        taskManager.createNewTask(task1);
        taskManager.createNewTask(task2);
        taskManager.createNewEpic(epic1);
        taskManager.createNewSubtask(subtask1);
        taskManager.createNewSubtask(subtask2);
        taskManager.createNewEpic(epic2);
        taskManager.createNewSubtask(subtask3);

        for (Task task : taskManager.getAllTaskTasks()) {
            System.out.println(task);
        }
        for (Epic epic : taskManager.getAllEpicTasks()){
            System.out.println(epic);
        }
        for (Subtask subtask : taskManager.getAllSubtaskTusks()){
            System.out.println(subtask);
        }

        task1 = new Task("first", "first task", 0, Status.IN_PROGRESS);
        task2 = new Task("second", "second task", 1, Status.DONE);
        subtask2 = new Subtask("fifth", "second subtask", 4, Status.IN_PROGRESS, epic1.getTaskId());
        subtask3 = new Subtask("seventh", "first subtask", 6, Status.DONE, epic2.getTaskId());

        taskManager.updateTaskTask(task1);
        taskManager.updateTaskTask(task2);
        taskManager.updateSubtaskTask(subtask2);
        taskManager.updateSubtaskTask(subtask3);

        for (Task task : taskManager.getAllTaskTasks()) {
            System.out.println(task);
        }
        for (Epic epic : taskManager.getAllEpicTasks()){
            System.out.println(epic);
        }
        for (Subtask subtask : taskManager.getAllSubtaskTusks()){
            System.out.println(subtask);
        }

        taskManager.deleteTaskTaskById(1);
        taskManager.deleteEpicTaskById(2);
    }
}
