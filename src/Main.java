import service.Managers;
import service.TaskManager;
import tasks.*;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = Managers.getDefault();
        Epic epic1 = new Epic("epic1", "epic1");
        Epic epic2 = new Epic("epic2", "epic2");
        taskManager.createNewEpic(epic1);
        taskManager.createNewEpic(epic2);
        Subtask subtask1 = new Subtask("sub1", "sub1", epic1.getTaskId());
        Subtask subtask2 = new Subtask("sub2", "sub2", epic1.getTaskId());
        Subtask subtask3 = new Subtask("sub3", "sub3", epic1.getTaskId());
        taskManager.createNewSubtask(subtask1);
        taskManager.createNewSubtask(subtask2);
        taskManager.createNewSubtask(subtask3);
        taskManager.getSubtask(subtask1.getTaskId());
        taskManager.getEpic(epic1.getTaskId());
        taskManager.getSubtask(subtask3.getTaskId());
        taskManager.getSubtask(subtask2.getTaskId());
        taskManager.getEpic(epic2.getTaskId());
        taskManager.getSubtask(subtask2.getTaskId());
        taskManager.getEpic(epic1.getTaskId());
        taskManager.getSubtask(subtask3.getTaskId());
        taskManager.getSubtask(subtask2.getTaskId());
        taskManager.getEpic(epic1.getTaskId());
        System.out.println(taskManager.getHistory());
        taskManager.deleteSubtask(subtask1.getTaskId());
        System.out.println(taskManager.getHistory());
        taskManager.deleteEpic(epic1.getTaskId());
        System.out.println(taskManager.getHistory());
    }
}
