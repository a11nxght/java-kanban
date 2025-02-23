package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    //    private FileBackedTaskManager taskManager;
    private Path path;

    @Override
    public FileBackedTaskManager getTaskManager() {
        return new FileBackedTaskManager(path);
    }

    @BeforeEach
    void setUp() throws IOException {
        path = File.createTempFile("data", null).toPath();
        taskManager = getTaskManager();

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
        DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy");
//        LocalDateTime firstTime = LocalDateTime.parse("10:13 23.02.25", DATE_TIME_FORMATTER);
        LocalDateTime secondTime = LocalDateTime.parse("11:00 23.02.25", DATE_TIME_FORMATTER);
//        LocalDateTime thirdTime = LocalDateTime.parse("22:46 23.02.25", DATE_TIME_FORMATTER);
        LocalDateTime fourthTime = LocalDateTime.parse("07:00 23.02.25", DATE_TIME_FORMATTER);
//        Task task1111 = new Task(Type.TASK, "t1", "t11");
//        task1111.setDuration(Duration.ofSeconds(100));
//        task1111.setStartTime(firstTime);
//        Task task222 = new Task(Type.TASK, "t2", "t22");
//        task222.setDuration(Duration.ofSeconds(100));
//        task222.setStartTime(thirdTime);
//        taskManager.createNewTask(task1111);
//        taskManager.createNewTask(task222);
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
        Epic epic2 = new Epic(Type.EPIC, "2", "2");
        Epic epic3 = new Epic(Type.EPIC, "3", "3");
        taskManager.createNewEpic(epic2);
        taskManager.createNewEpic(epic3);
        Subtask subtask3 = new Subtask(Type.SUBTASK, "s2", "s2", epic3.getTaskId());
        taskManager.createNewSubtask(subtask3);
        subtask3.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask3);
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(path.toFile());
//        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(new File("D:\\Java_projects\\java-kanban\\data5301599386035049526.tmp"));
        assertEquals(taskManager.getAllTasks(), fileBackedTaskManager.getAllTasks());
        assertEquals(taskManager.getAllEpics(), fileBackedTaskManager.getAllEpics());
        assertEquals(taskManager.getAllSubtasks(), fileBackedTaskManager.getAllSubtasks());
        for (Task task : taskManager.getAllTasks()) {
            Task backTask = fileBackedTaskManager.getTask(task.getTaskId());
            assertEquals(task.getName(), backTask.getName());
            assertEquals(task.getDescription(), backTask.getDescription());
            assertEquals(task.getStatus(), backTask.getStatus());
            assertEquals(task.getDuration(), backTask.getDuration());
            assertEquals(task.getStartTime(), backTask.getStartTime());
        }
        for (Epic epic : taskManager.getAllEpics()) {
            Epic backEpic = fileBackedTaskManager.getEpic(epic.getTaskId());
            assertEquals(epic.getName(), backEpic.getName());
            assertEquals(epic.getDescription(), backEpic.getDescription());
            assertEquals(epic.getStatus(), backEpic.getStatus());
            assertEquals(epic.getSubtasks(), backEpic.getSubtasks());
            assertEquals(epic.getDuration(), backEpic.getDuration());
            assertEquals(epic.getStartTime(), backEpic.getStartTime());
        }
        for (Subtask subtask : taskManager.getAllSubtasks()) {
            Subtask backSubtask = fileBackedTaskManager.getSubtask(subtask.getTaskId());
            assertEquals(subtask.getName(), backSubtask.getName());
            assertEquals(subtask.getDescription(), backSubtask.getDescription());
            assertEquals(subtask.getStatus(), backSubtask.getStatus());
            assertEquals(subtask.getEpicId(), backSubtask.getEpicId());
            assertEquals(subtask.getDuration(), backSubtask.getDuration());
            assertEquals(subtask.getStartTime(), backSubtask.getStartTime());
        }
    }

    @Test
    void testException() {
        assertDoesNotThrow(() -> {
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(File.createTempFile("data", null).toPath());
            Task task1 = new Task(Type.TASK, "first", "first task");
            fileBackedTaskManager.createNewTask(task1);
        });
    }


}