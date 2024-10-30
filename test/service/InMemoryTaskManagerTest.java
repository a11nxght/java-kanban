package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private TaskManager taskManager;
    private ArrayList<Integer> taskNumbers;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
        taskNumbers = new ArrayList<>();
    }

    @Test
    void createNewTask() {
        Task task1 = new Task("first", "first task");
        taskNumbers.add(taskManager.createNewTask(task1));
        assertNotEquals(0, task1.getTaskId());
    }

    @Test
    void createNewEpic() {
        Epic epic1 = new Epic("third", "first epic");
        taskNumbers.add(taskManager.createNewEpic(epic1));
        assertNotEquals(0, epic1.getTaskId());
    }

    @Test
    void createNewSubtask() {
        Epic epic1 = new Epic("third", "first epic");
        taskNumbers.add(taskManager.createNewEpic(epic1));
        Subtask subtask1 = new Subtask("forth", "first subtask", epic1.getTaskId());
        taskNumbers.add(taskManager.createNewSubtask(subtask1));
        assertNotEquals(0, subtask1.getTaskId());
    }

    @Test
    void updateTask() {
        Task task1 = new Task("first", "first task");
        taskNumbers.add(taskManager.createNewTask(task1));
        task1.setDescription("task first");
        task1.setName("tsrif");
        taskManager.updateTask(task1);
        Task updateTask = taskManager.getTask(task1.getTaskId());
        assertEquals("task first", updateTask.getDescription());
        assertEquals("tsrif", updateTask.getName());
    }

    @Test
    void getAllTasks() {
        Task task1 = new Task("1", "11");
        Task task2 = new Task("2", "22");
        Task task3 = new Task("3", "33");
        taskManager.createNewTask(task1);
        taskManager.createNewTask(task2);
        taskManager.createNewTask(task3);
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        assertArrayEquals(tasks.toArray(new Task[0]), taskManager.getAllTasks().toArray(new Task[0]));
    }

    @Test
    void deleteAllTasks() {
        Task task1 = new Task("1", "11");
        Task task2 = new Task("2", "22");
        Task task3 = new Task("3", "33");
        taskManager.createNewTask(task1);
        taskManager.createNewTask(task2);
        taskManager.createNewTask(task3);
        taskManager.deleteAllTasks();
        assertEquals(0, taskManager.getAllTasks().size());
    }

    @Test
    void deleteTask() {
        Task task1 = new Task("1", "11");
        Task task2 = new Task("2", "22");
        taskManager.createNewTask(task1);
        taskManager.createNewTask(task2);
        taskManager.deleteTask(task1.getTaskId());
        assertEquals(1, taskManager.getAllTasks().size());
    }

    @Test
    void getTask() {
        Task task1 = new Task("1", "11");
        Task task2 = new Task("2", "22");
        taskManager.createNewTask(task1);
        taskManager.createNewTask(task2);
        assertEquals("1", taskManager.getTask(task1.getTaskId()).getName());
        assertEquals("11", taskManager.getTask(task1.getTaskId()).getDescription());
        assertEquals(task1, taskManager.getTask(task1.getTaskId()));
    }

    @Test
    void getSubtasksFromEpic() {
        Epic epic = new Epic("1", "2");
        taskManager.createNewEpic(epic);
        Subtask subtask1 = new Subtask("s1", "s1", epic.getTaskId());
        Subtask subtask2 = new Subtask("s2", "s2", epic.getTaskId());
        taskManager.createNewSubtask(subtask1);
        taskManager.createNewSubtask(subtask2);
        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks.add(subtask1);
        subtasks.add(subtask2);
        assertArrayEquals(subtasks.toArray(new Task[0]), taskManager.getAllSubtasks().toArray(new Subtask[0]));
    }

    @Test
    void updateEpic() {
        Epic epic = new Epic("1", "2");
        taskManager.createNewEpic(epic);
        Epic epicUpdated = new Epic("1u", "1u", epic.getTaskId());
        taskManager.updateEpic(epicUpdated);
        assertEquals(epicUpdated.getName(), taskManager.getEpic(epicUpdated.getTaskId()).getName());
        assertEquals(epicUpdated.getDescription(), taskManager.getEpic(epicUpdated.getTaskId()).getDescription());
    }

    @Test
    void getAllEpics() {
        Epic epic1 = new Epic("1", "1");
        Epic epic2 = new Epic("2", "2");
        Epic epic3 = new Epic("3", "3");
        taskManager.createNewEpic(epic1);
        taskManager.createNewEpic(epic2);
        taskManager.createNewEpic(epic3);
        ArrayList<Epic> epics = new ArrayList<>();
        epics.add(epic1);
        epics.add(epic2);
        epics.add(epic3);
        assertArrayEquals(epics.toArray(new Epic[0]), taskManager.getAllEpics().toArray(new Epic[0]));
    }

    @Test
    void deleteAllEpics() {
        Epic epic1 = new Epic("1", "1");
        Epic epic2 = new Epic("2", "2");
        Epic epic3 = new Epic("3", "3");
        taskManager.createNewEpic(epic1);
        taskManager.createNewEpic(epic2);
        taskManager.createNewEpic(epic3);
        Subtask subtask1 = new Subtask("s1", "s1", epic1.getTaskId());
        Subtask subtask2 = new Subtask("s2", "s2", epic1.getTaskId());
        taskManager.createNewSubtask(subtask1);
        taskManager.createNewSubtask(subtask2);
        assertEquals(3, taskManager.getAllEpics().size());
        assertEquals(2, taskManager.getAllSubtasks().size());
        taskManager.deleteAllEpics();
        assertEquals(0, taskManager.getAllEpics().size());
        assertEquals(0, taskManager.getAllSubtasks().size());
    }

    @Test
    void deleteEpic() {
        Epic epic1 = new Epic("1", "1");
        Epic epic2 = new Epic("2", "2");
        Epic epic3 = new Epic("3", "3");
        taskManager.createNewEpic(epic1);
        taskManager.createNewEpic(epic2);
        taskManager.createNewEpic(epic3);
        Subtask subtask1 = new Subtask("s1", "s1", epic1.getTaskId());
        Subtask subtask2 = new Subtask("s2", "s2", epic1.getTaskId());
        taskManager.createNewSubtask(subtask1);
        taskManager.createNewSubtask(subtask2);
        taskManager.deleteEpic(epic2.getTaskId());
        assertNull(taskManager.getEpic(epic2.getTaskId()));
        assertEquals(2, taskManager.getAllEpics().size());
        assertEquals(2, taskManager.getAllSubtasks().size());
        taskManager.deleteEpic(epic1.getTaskId());
        assertEquals(1, taskManager.getAllEpics().size());
        assertEquals(0, taskManager.getAllSubtasks().size());
    }

    @Test
    void getEpic() {
        Epic epic1 = new Epic("1", "1");
        Epic epic2 = new Epic("2", "2");
        taskManager.createNewEpic(epic1);
        taskManager.createNewEpic(epic2);
        assertEquals(epic1, taskManager.getEpic(epic1.getTaskId()));
        assertNotEquals(epic2, taskManager.getEpic(epic1.getTaskId()));
    }

    @Test
    void updateSubtask() {
        Epic epic1 = new Epic("1", "1");
        taskManager.createNewEpic(epic1);
        Subtask subtask1 = new Subtask("s1", "s1", epic1.getTaskId());
        taskManager.createNewSubtask(subtask1);
        Subtask subtaskUpdated = new Subtask("u1", "u1", subtask1.getTaskId(), epic1.getTaskId());
        taskManager.updateSubtask(subtaskUpdated);
        assertEquals(subtaskUpdated.getName(), taskManager.getSubtask(subtask1.getTaskId()).getName());
        assertEquals(subtaskUpdated.getDescription(), taskManager.getSubtask(subtask1.getTaskId()).getDescription());
    }

    @Test
    void getAllSubtasks() {
        Epic epic1 = new Epic("1", "1");
        taskManager.createNewEpic(epic1);
        Subtask subtask1 = new Subtask("s1", "s1", epic1.getTaskId());
        Subtask subtask2 = new Subtask("s2", "s2", epic1.getTaskId());
        Subtask subtask3 = new Subtask("s3", "s3", epic1.getTaskId());
        taskManager.createNewSubtask(subtask1);
        taskManager.createNewSubtask(subtask2);
        taskManager.createNewSubtask(subtask3);
        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks.add(subtask1);
        subtasks.add(subtask2);
        subtasks.add(subtask3);
        assertArrayEquals(subtasks.toArray(new Subtask[0]), taskManager.getAllSubtasks().toArray(new Subtask[0]));
    }

    @Test
    void deleteAllSubtasks() {
        Epic epic1 = new Epic("1", "1");
        taskManager.createNewEpic(epic1);
        Subtask subtask1 = new Subtask("s1", "s1", epic1.getTaskId());
        Subtask subtask2 = new Subtask("s2", "s2", epic1.getTaskId());
        Subtask subtask3 = new Subtask("s3", "s3", epic1.getTaskId());
        taskManager.createNewSubtask(subtask1);
        taskManager.createNewSubtask(subtask2);
        taskManager.createNewSubtask(subtask3);
        assertEquals(3, taskManager.getAllSubtasks().size());
        taskManager.deleteAllSubtasks();
        assertEquals(0, taskManager.getAllSubtasks().size());
    }

    @Test
    void deleteSubtask() {
        Epic epic1 = new Epic("1", "1");
        taskManager.createNewEpic(epic1);
        Subtask subtask1 = new Subtask("s1", "s1", epic1.getTaskId());
        Subtask subtask2 = new Subtask("s2", "s2", epic1.getTaskId());
        Subtask subtask3 = new Subtask("s3", "s3", epic1.getTaskId());
        taskManager.createNewSubtask(subtask1);
        taskManager.createNewSubtask(subtask2);
        taskManager.createNewSubtask(subtask3);
        assertNotNull(taskManager.getSubtask(subtask1.getTaskId()));
        taskManager.deleteSubtask(subtask1.getTaskId());
        assertNull(taskManager.getSubtask(subtask1.getTaskId()));
        assertEquals(2, taskManager.getAllSubtasks().size());
    }

    @Test
    void getSubtask() {
        Epic epic1 = new Epic("1", "1");
        taskManager.createNewEpic(epic1);
        Subtask subtask1 = new Subtask("s1", "s1", epic1.getTaskId());
        taskManager.createNewSubtask(subtask1);
        assertNotNull(taskManager.getSubtask(subtask1.getTaskId()));
        assertEquals("s1", taskManager.getSubtask(subtask1.getTaskId()).getName());
        assertEquals("s1", taskManager.getSubtask(subtask1.getTaskId()).getDescription());
    }

    @Test
    void idNotConflict() {
        Task task = new Task("1", "2", 23);
        int id = taskManager.createNewTask(task);
        assertNotNull(taskManager.getTask(task.getTaskId()));
        assertEquals(id, taskManager.getTask(task.getTaskId()).getTaskId());
    }
}