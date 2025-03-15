package server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import service.Managers;
import service.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.Type;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    TaskManager taskManager = Managers.getDefault();
    HttpTaskServer taskServer = new HttpTaskServer(taskManager);
    Gson gson = HttpTaskServer.getGson();

    @BeforeEach
    void setUp() {
        taskManager.deleteAllTasks();
        taskManager.deleteAllSubtasks();
        taskManager.deleteAllEpics();
        taskServer.start();
    }

    @AfterEach
    void tearDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task(Type.TASK, "Test1", "Testing task 1",
                Duration.ofMinutes(5), LocalDateTime.now());
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        List<Task> tasksFromManager = taskManager.getAllTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test1", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    void testUpdateTask() throws IOException, InterruptedException {
        Task task = new Task(Type.TASK, "Test1", "Testing task 1",
                Duration.ofMinutes(5), LocalDateTime.now());
        taskManager.createNewTask(task);
        Task task2 = new Task(Type.TASK, "Test2", "Testing task 2", Duration.ofMinutes(5), LocalDateTime.now().plusHours(1));
        task2.setTaskId(1);
        String taskJson = gson.toJson(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertEquals(taskManager.getTask(1).getName(), task2.getName());
        assertEquals(taskManager.getTask(1).getDescription(), task2.getDescription());
        assertEquals(taskManager.getTask(1).getStartTime(), task2.getStartTime());
    }

    @Test
    void testGetTasks() throws IOException, InterruptedException {
        Task task1 = new Task(Type.TASK, "Test1", "Testing task 1", Duration.ofMinutes(5), LocalDateTime.now());
        Task task2 = new Task(Type.TASK, "Test2", "Testing task 2", Duration.ofMinutes(5), LocalDateTime.now().plusHours(1));
        taskManager.createNewTask(task1);
        taskManager.createNewTask(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        Task task = gson.fromJson(jsonArray.get(0), Task.class);
        assertEquals(task1, task);
        assertEquals("Testing task 2", jsonArray.get(1).getAsJsonObject().get("description").getAsString());
    }

    @Test
    void testGetTask() throws IOException, InterruptedException {
        Task task1 = new Task(Type.TASK, "Test1", "Testing task 1", Duration.ofMinutes(5), LocalDateTime.now());
        Task task2 = new Task(Type.TASK, "Test2", "Testing task 2", Duration.ofMinutes(5), LocalDateTime.now().plusHours(1));
        taskManager.createNewTask(task1);
        taskManager.createNewTask(task2);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/2");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertEquals("Testing task 2", jsonElement.getAsJsonObject().get("description").getAsString());
        Task task = gson.fromJson(jsonElement.getAsJsonObject(), Task.class);
        assertEquals(task2, task);
    }

    @Test
    void testDeleteTask() throws IOException, InterruptedException {
        Task task1 = new Task(Type.TASK, "Test1", "Testing task 1", Duration.ofMinutes(5), LocalDateTime.now());
        Task task2 = new Task(Type.TASK, "Test2", "Testing task 2", Duration.ofMinutes(5), LocalDateTime.now().plusHours(1));
        taskManager.createNewTask(task1);
        taskManager.createNewTask(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> tasksFromManager = taskManager.getAllTasks();
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test2", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    void testGetSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic(Type.EPIC, "epic1", "epic1");
        taskManager.createNewEpic(epic);
        Subtask subtask1 = new Subtask(Type.SUBTASK, "subtask1", "subtask1", 1);
        Subtask subtask2 = new Subtask(Type.SUBTASK, "subtask2", "subtask2", 1);
        taskManager.createNewSubtask(subtask1);
        taskManager.createNewSubtask(subtask2);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        Subtask subtask = gson.fromJson(jsonArray.get(0), Subtask.class);
        assertEquals(subtask1, subtask);
        assertEquals("subtask2", jsonArray.get(1).getAsJsonObject().get("description").getAsString());
    }

    @Test
    void testGetSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic(Type.EPIC, "epic1", "epic1");
        taskManager.createNewEpic(epic);
        Subtask subtask1 = new Subtask(Type.SUBTASK, "subtask1", "subtask1", 1);
        Subtask subtask2 = new Subtask(Type.SUBTASK, "subtask2", "subtask2", 1);
        taskManager.createNewSubtask(subtask1);
        taskManager.createNewSubtask(subtask2);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertEquals("subtask1", jsonElement.getAsJsonObject().get("description").getAsString());
        Subtask subtask = gson.fromJson(jsonElement.getAsJsonObject(), Subtask.class);
        assertEquals(subtask1, subtask);
    }

    @Test
    void testAddSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic(Type.EPIC, "epic1", "epic1");
        taskManager.createNewEpic(epic);
        Subtask subtask1 = new Subtask(Type.SUBTASK, "subtask1", "subtask1", 1);
        String subtaskJson = gson.toJson(subtask1);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        List<Subtask> subtasksFromManager = taskManager.getAllSubtasks();

        assertNotNull(subtasksFromManager, "Задачи не возвращаются");
        assertEquals(1, subtasksFromManager.size(), "Некорректное количество задач");
        assertEquals("subtask1", subtasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    void testUpdateSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic(Type.EPIC, "epic1", "epic1");
        taskManager.createNewEpic(epic);
        Subtask subtask1 = new Subtask(Type.SUBTASK, "subtask1", "subtask1", 1);
        taskManager.createNewSubtask(subtask1);
        Subtask updatedSubtask = new Subtask(Type.SUBTASK, "subtask1upd", "subtask1upd",
                subtask1.getTaskId(), epic.getTaskId());

        String subtaskJson = gson.toJson(updatedSubtask);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        List<Subtask> subtasksFromManager = taskManager.getAllSubtasks();

        assertNotNull(subtasksFromManager, "Задачи не возвращаются");
        assertEquals(1, subtasksFromManager.size(), "Некорректное количество задач");
        assertEquals("subtask1upd", subtasksFromManager.getFirst().getName(), "Некорректное имя задачи");

    }

    @Test
    void testDeleteSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic(Type.EPIC, "epic1", "epic1");
        taskManager.createNewEpic(epic);
        Subtask subtask1 = new Subtask(Type.SUBTASK, "subtask1", "subtask1", 1);
        taskManager.createNewSubtask(subtask1);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(0, taskManager.getAllSubtasks().size());
    }

    @Test
    void testGetEpics() throws IOException, InterruptedException {
        Epic epic1 = new Epic(Type.EPIC, "epic1", "epic1");
        Epic epic2 = new Epic(Type.EPIC, "epic2", "epic2");
        taskManager.createNewEpic(epic1);
        taskManager.createNewEpic(epic2);
        Subtask subtask1 = new Subtask(Type.SUBTASK, "subtask1", "subtask1", 1);
        Subtask subtask2 = new Subtask(Type.SUBTASK, "subtask2", "subtask2", 1);
        taskManager.createNewSubtask(subtask1);
        taskManager.createNewSubtask(subtask2);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        Epic epic = gson.fromJson(jsonArray.get(0), Epic.class);
        assertEquals(epic1, epic);
        assertEquals("epic2", jsonArray.get(1).getAsJsonObject().get("description").getAsString());
    }

    @Test
    void testGetEpic() throws IOException, InterruptedException {
        Epic epic1 = new Epic(Type.EPIC, "epic1", "epic1");
        Epic epic2 = new Epic(Type.EPIC, "epic2", "epic2");
        taskManager.createNewEpic(epic1);
        taskManager.createNewEpic(epic2);
        Subtask subtask1 = new Subtask(Type.SUBTASK, "subtask1", "subtask1", 1);
        Subtask subtask2 = new Subtask(Type.SUBTASK, "subtask2", "subtask2", 1);
        taskManager.createNewSubtask(subtask1);
        taskManager.createNewSubtask(subtask2);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertEquals("epic1", jsonElement.getAsJsonObject().get("description").getAsString());
        Epic epic = gson.fromJson(jsonElement.getAsJsonObject(), Epic.class);
        assertEquals(epic1, epic);
    }

    @Test
    void testGetEpicSubtasks() throws IOException, InterruptedException {
        Epic epic1 = new Epic(Type.EPIC, "epic1", "epic1");
        Epic epic2 = new Epic(Type.EPIC, "epic2", "epic2");
        taskManager.createNewEpic(epic1);
        taskManager.createNewEpic(epic2);
        Subtask subtask1 = new Subtask(Type.SUBTASK, "subtask1", "subtask1", 1);
        Subtask subtask2 = new Subtask(Type.SUBTASK, "subtask2", "subtask2", 1);
        taskManager.createNewSubtask(subtask1);
        taskManager.createNewSubtask(subtask2);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics/1/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        Subtask subtask = gson.fromJson(jsonArray.get(0), Subtask.class);
        assertEquals(subtask1, subtask);
        assertEquals("subtask2", jsonArray.get(1).getAsJsonObject().get("description").getAsString());
    }

    @Test
    void testAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic(Type.EPIC, "epic1", "epic1");
        String epicJson = gson.toJson(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        List<Epic> epicsFromManager = taskManager.getAllEpics();

        assertNotNull(epicsFromManager, "Задачи не возвращаются");
        assertEquals(1, epicsFromManager.size(), "Некорректное количество задач");
        assertEquals("epic1", epicsFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    void testDeleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic(Type.EPIC, "epic1", "epic1");
        taskManager.createNewEpic(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(0, taskManager.getAllEpics().size());
    }

    @Test
    void testGetHistory() throws IOException, InterruptedException {
        Task task1 = new Task(Type.TASK, "task1", "test", Duration.ZERO, LocalDateTime.now());
        Task task2 = new Task(Type.TASK, "task2", "test2", Duration.ZERO, LocalDateTime.now().plusHours(1));
        taskManager.createNewTask(task1);
        taskManager.createNewTask(task2);
        Epic epic = new Epic(Type.EPIC, "epic1", "epic1");
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(new Subtask(Type.SUBTASK, "subtask1", "subtask1", 3));
        taskManager.getTask(1);
        taskManager.getSubtask(4);
        taskManager.getEpic(3);
        taskManager.getTask(2);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        Task task3 = gson.fromJson(jsonArray.get(0), Task.class);
        assertEquals(task1, task3);
        Epic epic1 = gson.fromJson(jsonArray.get(2), Epic.class);
        assertEquals(epic1.getDescription(), epic.getDescription());
    }

    @Test
    void testGetPrioritizedTasks() throws IOException, InterruptedException {
        Task task1 = new Task(Type.TASK, "task1", "test", Duration.ofMinutes(10), LocalDateTime.now());
        Task task2 = new Task(Type.TASK, "task2", "test2", Duration.ofMinutes(5), LocalDateTime.now().plusHours(1));
        Epic epic = new Epic(Type.EPIC, "epic1", "epic1");
        taskManager.createNewTask(task1);
        taskManager.createNewTask(task2);
        taskManager.createNewEpic(epic);
        Subtask subtask = new Subtask(Type.SUBTASK, "subtask1", "subtask1",
                Duration.ofMinutes(20), LocalDateTime.now().minusHours(2), epic.getTaskId());
        taskManager.createNewSubtask(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        Subtask subtask1 = gson.fromJson(jsonArray.get(0), Subtask.class);
        assertEquals(subtask, subtask1);
        Task task3 = gson.fromJson(jsonArray.get(1), Task.class);
        assertEquals(task1.getName(), task3.getName());
    }
}