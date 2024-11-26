package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest {

    private FileBackedTaskManager taskManager;
    private ArrayList<Integer> taskNumbers;
    private Path path;

    @BeforeEach
    void setUp() throws IOException {
        Path path1 = File.createTempFile("data", null).toPath();
        taskManager = new FileBackedTaskManager(path1);
        taskNumbers = new ArrayList<>();
        path = path1;
    }

    @Test
    void loadFromEmptyFile() {

    }

    @Test
    void saveAndLoadEmptyFile() {
        taskManager.save();
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(path.toFile());
    }
    @Test
    void save() {
        taskManager.save();
        Task task1 = new Task("first", "first task");
        Task task2 = new Task("second", "second task");
        taskNumbers.add(taskManager.createNewTask(task1));
        taskNumbers.add(taskManager.createNewTask(task2));
    }

    @Test
    void fromString() {
        Task task1 = new Task("first", "first task");
        Task task2 = new Task("second", "second task");
        taskNumbers.add(taskManager.createNewTask(task1));
        taskNumbers.add(taskManager.createNewTask(task2));
        task2.setStatus(Status.DONE);
        Epic epic = new Epic("1", "2");
        taskManager.createNewEpic(epic);
        Subtask subtask1 = new Subtask("s1", "s1", epic.getTaskId());
        Subtask subtask2 = new Subtask("s2", "s2", epic.getTaskId());
        taskManager.createNewSubtask(subtask1);
        taskManager.createNewSubtask(subtask2);
        Epic epic2 = new Epic("2", "2");
        Epic epic3 = new Epic("3", "3");
        taskManager.createNewEpic(epic2);
        taskManager.createNewEpic(epic3);
        Subtask subtask3 = new Subtask("s2", "s2", epic3.getTaskId());
        taskManager.createNewSubtask(subtask3);
        subtask3.setStatus(Status.DONE);
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(path.getFileName().toFile());
        assertEquals(taskManager.getAllTasks(), fileBackedTaskManager.getAllTasks());
        assertEquals(taskManager.getAllEpics(), fileBackedTaskManager.getAllEpics());
        assertEquals(taskManager.getAllSubtasks(), fileBackedTaskManager.getAllSubtasks());
    }
}