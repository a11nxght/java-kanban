package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import service.Managers;
import service.TaskManager;
import tasks.Task;
import tasks.Type;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HttpTaskServer {
    private static final int PORT = 8080;


    public static void main(String[] args) throws IOException {
//        String jsonString = "{\n" +
//                "\t\"name\": \"task3\",\n" +
//                "\t\"description\": \"test3\",\n" +
//                "\t\"type\": \"TASK\"\n" +
//                "}";
//        Gson gson = new Gson();
//        Task taskj = gson.fromJson(jsonString, Task.class);
        TaskManager taskManager = Managers.getDefault();
        taskManager.createNewTask(new Task(Type.TASK, "task1", "test", Duration.ZERO, LocalDateTime.now()));
        Task task = new Task(Type.TASK, "task2", "test2", Duration.ZERO, LocalDateTime.now());
        taskManager.createNewTask(task);

        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new HttpTasksHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtasksHandler());
        httpServer.createContext("/epics", new EpicsHandler());
        httpServer.createContext("/history", new HistoryHandler());
        httpServer.createContext("/prioritized", new PrioritizedHandler());
        httpServer.start();
    }
}

class EpicsHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}

class SubtasksHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}

class HistoryHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}

class PrioritizedHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}


