import tasks.Task;
import tasks.Epic;
import tasks.Subtask;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        Task task = new Task("Task", "Task", 1);
        Epic epic = new Epic("Epic", "Epic", 2);
        Subtask subtask = new Subtask("Subtask", "Subtask", 3, 2);

        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task);
        tasks.add(epic);
        tasks.add(subtask);
        for (Task task1 : tasks) {
            if (task1.getClass() == Subtask.class){
                System.out.println(((Subtask) task1).getEpicId());
            }
        }
    }
}
