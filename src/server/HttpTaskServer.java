package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final TaskManager taskManager;
    private HttpServer httpServer;

    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        HttpTaskServer taskServer = new HttpTaskServer(taskManager);
        taskServer.start();
    }

    public void start() {
        try {
            httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(PORT), 0);
            httpServer.createContext("/tasks", new HttpTasksHandler(taskManager));
            httpServer.createContext("/subtasks", new HttpSubtasksHandler(taskManager));
            httpServer.createContext("/epics", new HttpEpicsHandler(taskManager));
            httpServer.createContext("/history", new HttpHistoryHandler(taskManager));
            httpServer.createContext("/prioritized", new HttpPrioritizedHandler(taskManager));
            httpServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        httpServer.stop(0);
    }

    public static Gson getGson() {
        return new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }
}
