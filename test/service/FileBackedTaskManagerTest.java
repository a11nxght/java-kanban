package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest {

    private FileBackedTaskManager taskManager;
    private Path path;

    @BeforeEach
    void setUp() throws IOException {
        Path path1 = File.createTempFile("data", null).toPath();
        taskManager = new FileBackedTaskManager(path1);
        path = path1;
    }

    @Test
    void loadFromEmptyFile() {
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(path.toFile());
    }

    @Test
    void saveEmptyFile() {
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(path.toFile());
        fileBackedTaskManager.deleteAllTasks();
    }

    @Test
    void fromString() {
        Task task1 = new Task(Type.TASK, "first", "first task");
        Task task2 = new Task(Type.TASK, "second", "second task");
        taskManager.createNewTask(task1);
        taskManager.createNewTask(task2);
        task2.setStatus(Status.DONE);
        Epic epic1 = new Epic(Type.EPIC, "1", "2");
        taskManager.createNewEpic(epic1);
        Subtask subtask1 = new Subtask(Type.SUBTASK, "s1", "s1", epic1.getTaskId());
        Subtask subtask2 = new Subtask(Type.SUBTASK, "s2", "s2", epic1.getTaskId());
        taskManager.createNewSubtask(subtask1);
        taskManager.createNewSubtask(subtask2);
        Epic epic2 = new Epic(Type.EPIC, "2", "2");
        Epic epic3 = new Epic(Type.EPIC, "3", "3");
        taskManager.createNewEpic(epic2);
        taskManager.createNewEpic(epic3);
        Subtask subtask3 = new Subtask(Type.SUBTASK, "s2", "s2", epic3.getTaskId());
        taskManager.createNewSubtask(subtask3);
        subtask3.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask3);
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(path.getFileName().toFile());
        assertEquals(taskManager.getAllTasks(), fileBackedTaskManager.getAllTasks());
        assertEquals(taskManager.getAllEpics(), fileBackedTaskManager.getAllEpics());
        assertEquals(taskManager.getAllSubtasks(), fileBackedTaskManager.getAllSubtasks());
        for (Task task : taskManager.getAllTasks()) {
            Task backTask = fileBackedTaskManager.getTask(task.getTaskId());
            assertEquals(task.getName(), backTask.getName());
            assertEquals(task.getDescription(), backTask.getDescription());
            assertEquals(task.getStatus(), task.getStatus());
        }
        for (Epic epic : taskManager.getAllEpics()) {
            Epic backEpic = fileBackedTaskManager.getEpic(epic.getTaskId());
            assertEquals(epic.getName(), backEpic.getName());
            assertEquals(epic.getDescription(), backEpic.getDescription());
            assertEquals(epic.getStatus(), backEpic.getStatus());
            assertEquals(epic.getSubtasks(), epic.getSubtasks());
        }
        for (Subtask subtask : taskManager.getAllSubtasks()) {
            Subtask backSubtask = fileBackedTaskManager.getSubtask(subtask.getTaskId());
            assertEquals(subtask.getName(), backSubtask.getName());
            assertEquals(subtask.getDescription(), backSubtask.getDescription());
            assertEquals(subtask.getStatus(), backSubtask.getStatus());
            assertEquals(subtask.getEpicId(), backSubtask.getEpicId());
        }
    }
}