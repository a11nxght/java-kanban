import service.TaskManager;
import tasks.*;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();
        ArrayList<Integer> taskNumbers = new ArrayList<>();
        Task task1 = new Task("first", "first task");
        Task task2 = new Task("second", "second task");
        Epic epic1 = new Epic("third", "first epic");
        Subtask subtask1 = new Subtask("forth", "first subtask", 2);
        Subtask subtask2 = new Subtask("fifth", "second subtask", 2);
        Epic epic2 = new Epic("sixth", "second epic");
        Subtask subtask3 = new Subtask("seventh", "first subtask", 5);

        taskNumbers.add(taskManager.createNewTask(task1));
        taskNumbers.add(taskManager.createNewTask(task2));
        taskNumbers.add(taskManager.createNewEpic(epic1));
        taskNumbers.add(taskManager.createNewSubtask(subtask1));
        taskNumbers.add(taskManager.createNewSubtask(subtask2));
        taskNumbers.add(taskManager.createNewEpic(epic2));
        taskNumbers.add(taskManager.createNewSubtask(subtask3));
        System.out.println(taskNumbers);
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
