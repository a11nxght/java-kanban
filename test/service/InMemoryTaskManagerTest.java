package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.Type;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private TaskManager taskManager;
    private ArrayList<Integer> taskNumbers;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
        taskNumbers = new ArrayList<>();
        now = LocalDateTime.now();
    }

    @Test
    void createNewTask() {
        Task task1 = new Task(Type.TASK, "first", "first task");
        task1.setStartTime(LocalDateTime.now());
        task1.setDuration(Duration.ofSeconds(60));
        taskNumbers.add(taskManager.createNewTask(task1));
        assertNotEquals(0, task1.getTaskId());
    }

    @Test
    void createNewEpic() {
        Epic epic1 = new Epic(Type.EPIC, "third", "first epic");
        taskNumbers.add(taskManager.createNewEpic(epic1));
        assertNotEquals(0, epic1.getTaskId());
    }

    @Test
    void createNewSubtask() {
        Epic epic1 = new Epic(Type.EPIC, "third", "first epic");
        taskNumbers.add(taskManager.createNewEpic(epic1));
        Subtask subtask1 = new Subtask(Type.SUBTASK, "forth", "first subtask", epic1.getTaskId());
        subtask1.setDuration(Duration.ofSeconds(60));
        subtask1.setStartTime(now);
        taskNumbers.add(taskManager.createNewSubtask(subtask1));
        assertNotEquals(0, subtask1.getTaskId());
        assertEquals(taskManager.getEpic(epic1.getTaskId()).getEndTime(), subtask1.getEndTime());
    }

    @Test
    void updateTask() {
        Task task1 = new Task(Type.TASK, "first", "first task");
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
        Task task1 = new Task(Type.TASK, "1", "11");
        Task task2 = new Task(Type.TASK, "2", "22");
        Task task3 = new Task(Type.TASK, "3", "33");
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
        Task task1 = new Task(Type.TASK, "1", "11");
        Task task2 = new Task(Type.TASK, "2", "22");
        Task task3 = new Task(Type.TASK, "3", "33");
        taskManager.createNewTask(task1);
        taskManager.createNewTask(task2);
        taskManager.createNewTask(task3);
        taskManager.deleteAllTasks();
        assertEquals(0, taskManager.getAllTasks().size());
    }

    @Test
    void deleteTask() {
        Task task1 = new Task(Type.TASK, "1", "11");
        Task task2 = new Task(Type.TASK, "2", "22");
        taskManager.createNewTask(task1);
        taskManager.createNewTask(task2);
        taskManager.deleteTask(task1.getTaskId());
        assertEquals(1, taskManager.getAllTasks().size());
    }

    @Test
    void getTask() {
        Task task1 = new Task(Type.TASK, "1", "11");
        Task task2 = new Task(Type.TASK, "2", "22");
        taskManager.createNewTask(task1);
        taskManager.createNewTask(task2);
        assertEquals("1", taskManager.getTask(task1.getTaskId()).getName());
        assertEquals("11", taskManager.getTask(task1.getTaskId()).getDescription());
        assertEquals(task1, taskManager.getTask(task1.getTaskId()));
    }

    @Test
    void getSubtasksFromEpic() {
        Epic epic = new Epic(Type.EPIC, "1", "2");
        taskManager.createNewEpic(epic);
        Subtask subtask1 = new Subtask(Type.SUBTASK, "s1", "s1", epic.getTaskId());
        Subtask subtask2 = new Subtask(Type.SUBTASK, "s2", "s2", epic.getTaskId());
        taskManager.createNewSubtask(subtask1);
        taskManager.createNewSubtask(subtask2);
        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks.add(subtask1);
        subtasks.add(subtask2);
        assertArrayEquals(subtasks.toArray(new Task[0]), taskManager.getAllSubtasks().toArray(new Subtask[0]));
    }

    @Test
    void updateEpic() {
        Epic epic = new Epic(Type.EPIC, "1", "2");
        taskManager.createNewEpic(epic);
        Epic epicUpdated = new Epic(Type.EPIC, "1u", "1u", epic.getTaskId());
        taskManager.updateEpic(epicUpdated);
        assertEquals(epicUpdated.getName(), taskManager.getEpic(epicUpdated.getTaskId()).getName());
        assertEquals(epicUpdated.getDescription(), taskManager.getEpic(epicUpdated.getTaskId()).getDescription());
    }

    @Test
    void getAllEpics() {
        Epic epic1 = new Epic(Type.EPIC, "1", "1");
        Epic epic2 = new Epic(Type.EPIC, "2", "2");
        Epic epic3 = new Epic(Type.EPIC, "3", "3");
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
        Epic epic1 = new Epic(Type.EPIC, "1", "1");
        Epic epic2 = new Epic(Type.EPIC, "2", "2");
        Epic epic3 = new Epic(Type.EPIC, "3", "3");
        taskManager.createNewEpic(epic1);
        taskManager.createNewEpic(epic2);
        taskManager.createNewEpic(epic3);
        Subtask subtask1 = new Subtask(Type.SUBTASK, "s1", "s1", epic1.getTaskId());
        Subtask subtask2 = new Subtask(Type.SUBTASK, "s2", "s2", epic1.getTaskId());
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
        Epic epic1 = new Epic(Type.EPIC, "1", "1");
        Epic epic2 = new Epic(Type.EPIC, "2", "2");
        Epic epic3 = new Epic(Type.EPIC, "3", "3");
        taskManager.createNewEpic(epic1);
        taskManager.createNewEpic(epic2);
        taskManager.createNewEpic(epic3);
        Subtask subtask1 = new Subtask(Type.SUBTASK, "s1", "s1", epic1.getTaskId());
        Subtask subtask2 = new Subtask(Type.SUBTASK, "s2", "s2", epic1.getTaskId());
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
        Epic epic1 = new Epic(Type.EPIC, "1", "1");
        Epic epic2 = new Epic(Type.EPIC, "2", "2");
        taskManager.createNewEpic(epic1);
        taskManager.createNewEpic(epic2);
        assertEquals(epic1, taskManager.getEpic(epic1.getTaskId()));
        assertNotEquals(epic2, taskManager.getEpic(epic1.getTaskId()));
    }

    @Test
    void updateSubtask() {
        Epic epic1 = new Epic(Type.EPIC, "1", "1");
        taskManager.createNewEpic(epic1);
        Subtask subtask1 = new Subtask(Type.SUBTASK, "s1", "s1", epic1.getTaskId());
        taskManager.createNewSubtask(subtask1);
        Subtask subtaskUpdated = new Subtask(Type.SUBTASK, "u1", "u1", subtask1.getTaskId(), epic1.getTaskId());
        subtaskUpdated.setDuration(Duration.ofSeconds(68));
        subtaskUpdated.setStartTime(now);
        taskManager.updateSubtask(subtaskUpdated);

        Subtask subtask2 = new Subtask(Type.SUBTASK, "s2", "s2", epic1.getTaskId());
        taskManager.createNewSubtask(subtask2);
        Subtask subtaskUpdated2 = new Subtask(Type.SUBTASK, "u1", "u1", subtask1.getTaskId(), epic1.getTaskId());
        subtaskUpdated2.setDuration(Duration.ofSeconds(68));
        taskManager.updateSubtask(subtaskUpdated2);

        assertEquals(subtaskUpdated.getName(), taskManager.getSubtask(subtask1.getTaskId()).getName());
        assertEquals(subtaskUpdated.getDescription(), taskManager.getSubtask(subtask1.getTaskId()).getDescription());
    }

    @Test
    void getAllSubtasks() {
        Epic epic1 = new Epic(Type.EPIC, "1", "1");
        taskManager.createNewEpic(epic1);
        Subtask subtask1 = new Subtask(Type.SUBTASK, "s1", "s1", epic1.getTaskId());
        Subtask subtask2 = new Subtask(Type.SUBTASK, "s2", "s2", epic1.getTaskId());
        Subtask subtask3 = new Subtask(Type.SUBTASK, "s3", "s3", epic1.getTaskId());
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
        Epic epic1 = new Epic(Type.EPIC, "1", "1");
        taskManager.createNewEpic(epic1);
        Subtask subtask1 = new Subtask(Type.SUBTASK, "s1", "s1", epic1.getTaskId());
        Subtask subtask2 = new Subtask(Type.SUBTASK, "s2", "s2", epic1.getTaskId());
        Subtask subtask3 = new Subtask(Type.SUBTASK, "s3", "s3", epic1.getTaskId());
        taskManager.createNewSubtask(subtask1);
        taskManager.createNewSubtask(subtask2);
        taskManager.createNewSubtask(subtask3);
        assertEquals(3, taskManager.getAllSubtasks().size());
        taskManager.deleteAllSubtasks();
        assertEquals(0, taskManager.getAllSubtasks().size());
    }

    @Test
    void deleteSubtask() {
        Epic epic1 = new Epic(Type.EPIC, "1", "1");
        taskManager.createNewEpic(epic1);
        Subtask subtask1 = new Subtask(Type.SUBTASK, "s1", "s1", epic1.getTaskId());
        Subtask subtask2 = new Subtask(Type.SUBTASK, "s2", "s2", epic1.getTaskId());
        Subtask subtask3 = new Subtask(Type.SUBTASK, "s3", "s3", epic1.getTaskId());
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
        Epic epic1 = new Epic(Type.EPIC, "1", "1");
        taskManager.createNewEpic(epic1);
        Subtask subtask1 = new Subtask(Type.SUBTASK, "s1", "s1", epic1.getTaskId());
        taskManager.createNewSubtask(subtask1);
        assertNotNull(taskManager.getSubtask(subtask1.getTaskId()));
        assertEquals("s1", taskManager.getSubtask(subtask1.getTaskId()).getName());
        assertEquals("s1", taskManager.getSubtask(subtask1.getTaskId()).getDescription());
    }

    @Test
    void idNotConflict() {
        Task task = new Task(Type.TASK, "1", "2", 23);
        int id = taskManager.createNewTask(task);
        assertNotNull(taskManager.getTask(task.getTaskId()));
        assertEquals(id, taskManager.getTask(task.getTaskId()).getTaskId());
    }

    @Test
    void tasksDeleteFromHistory() {
        TaskManager taskManager = Managers.getDefault();
        Epic epic1 = new Epic(Type.EPIC, "epic1", "epic1");
        Epic epic2 = new Epic(Type.EPIC, "epic2", "epic2");
        taskManager.createNewEpic(epic1);
        taskManager.createNewEpic(epic2);
        Subtask subtask1 = new Subtask(Type.SUBTASK, "sub1", "sub1", epic1.getTaskId());
        Subtask subtask2 = new Subtask(Type.SUBTASK, "sub2", "sub2", epic1.getTaskId());
        Subtask subtask3 = new Subtask(Type.SUBTASK, "sub3", "sub3", epic1.getTaskId());
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
        assertEquals(5, taskManager.getHistory().size());
        taskManager.deleteSubtask(subtask1.getTaskId());
        assertEquals(4, taskManager.getHistory().size());
        taskManager.deleteEpic(epic1.getTaskId());
        assertEquals(1, taskManager.getHistory().size());
        assertEquals(epic2, taskManager.getHistory().getFirst());
    }

    @Test
    void getPrioritizedTasks() {
        DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy");
        LocalDateTime firstTime = LocalDateTime.parse("07:13 23.02.25", DATE_TIME_FORMATTER);
        LocalDateTime secondTime = LocalDateTime.parse("11:00 23.02.25", DATE_TIME_FORMATTER);
        LocalDateTime thirdTime = LocalDateTime.parse("22:46 23.02.25", DATE_TIME_FORMATTER);
        LocalDateTime fourthTime = LocalDateTime.parse("23:04 23.02.25", DATE_TIME_FORMATTER);
        Task task1 = new Task(Type.TASK, "first", "first task");
        task1.setDuration(Duration.ofSeconds(100));
        task1.setStartTime(firstTime);
        Task task2 = new Task(Type.TASK, "second", "second task");
        task2.setDuration(Duration.ofSeconds(100));
        task2.setStartTime(thirdTime);
        taskManager.createNewTask(task2);
        taskManager.createNewTask(task1);
        Epic epic1 = new Epic(Type.EPIC, "1", "2");
        taskManager.createNewEpic(epic1);
        Subtask subtask1 = new Subtask(Type.SUBTASK, "s1", "s1", epic1.getTaskId());
        Subtask subtask2 = new Subtask(Type.SUBTASK, "s2", "s2", epic1.getTaskId());
        subtask1.setDuration(Duration.ofSeconds(170));
        subtask2.setDuration(Duration.ofSeconds(111));
        subtask1.setStartTime(secondTime);
        subtask2.setStartTime(fourthTime);
        taskManager.createNewSubtask(subtask1);
        taskManager.createNewSubtask(subtask2);
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(firstTime.toEpochSecond(ZoneOffset.ofHours(3)), prioritizedTasks.getFirst().getStartTime().toEpochSecond(ZoneOffset.ofHours(3)));
        assertEquals(secondTime.toEpochSecond(ZoneOffset.ofHours(3)), prioritizedTasks.get(1).getStartTime().toEpochSecond(ZoneOffset.ofHours(3)));
        assertEquals(thirdTime.toEpochSecond(ZoneOffset.ofHours(3)), prioritizedTasks.get(2).getStartTime().toEpochSecond(ZoneOffset.ofHours(3)));
        assertEquals(fourthTime.toEpochSecond(ZoneOffset.ofHours(3)), prioritizedTasks.get(3).getStartTime().toEpochSecond(ZoneOffset.ofHours(3)));
    }
}