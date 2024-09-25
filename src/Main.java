import service.TaskManager;
import tasks.*;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();
        ArrayList<Integer> taskNumbers = new ArrayList<>();
        Task task1 = new Task("first", "first task");
        taskNumbers.add(taskManager.createNewTask(task1));
        Task task2 = new Task("second", "second task");
        taskNumbers.add(taskManager.createNewTask(task2));
        Epic epic1 = new Epic("third", "first epic");
        taskNumbers.add(taskManager.createNewEpic(epic1));
        Subtask subtask1 = new Subtask("forth", "first subtask", epic1.getTaskId());
        taskNumbers.add(taskManager.createNewSubtask(subtask1));
        Subtask subtask2 = new Subtask("fifth", "second subtask", epic1.getTaskId());
        taskNumbers.add(taskManager.createNewSubtask(subtask2));
        Epic epic2 = new Epic("sixth", "second epic");
        taskNumbers.add(taskManager.createNewEpic(epic2));
        Subtask subtask3 = new Subtask("seventh", "first subtask", epic2.getTaskId());
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

        task1 = new Task("first", "first task", task1.getTaskId(), Status.IN_PROGRESS);
        taskManager.updateTaskTask(task1);
        task2 = new Task("second", "second task", task2.getTaskId(), Status.DONE);
        taskManager.updateTaskTask(task2);
        subtask2 = new Subtask("fifth", "second subtask", subtask2.getTaskId(), Status.IN_PROGRESS, epic1.getTaskId());
        taskManager.updateSubtaskTask(subtask2);
        subtask3 = new Subtask("seventh", "first subtask", subtask3.getTaskId(), Status.DONE, epic2.getTaskId());
        taskManager.updateSubtaskTask(subtask3);
        System.out.println("epic2 = " + epic2);

        for (Task task : taskManager.getAllTaskTasks()) {
            System.out.println(task);
        }
        for (Epic epic : taskManager.getAllEpicTasks()){
            System.out.println(epic);
        }
        for (Subtask subtask : taskManager.getAllSubtaskTusks()){
            System.out.println(subtask);
        }

        taskManager.deleteTaskTaskById(task1.getTaskId());
        taskManager.deleteEpicTaskById(epic1.getTaskId());
    }
}
