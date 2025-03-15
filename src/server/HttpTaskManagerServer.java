package server;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import service.Managers;
import service.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.Type;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskManagerServer {
    private static final int PORT = 8080;


    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefault();
        taskManager.createNewTask(new Task(Type.TASK, "task1", "test", Duration.ZERO, LocalDateTime.now()));
        Task task = new Task(Type.TASK, "task2", "test2", Duration.ZERO, LocalDateTime.now());
        taskManager.createNewTask(task);
        taskManager.createNewEpic(new Epic(Type.EPIC, "epic1", "epic1"));
        taskManager.createNewSubtask(new Subtask(Type.SUBTASK, "subtask1", "subtask1", 3));
        taskManager.getTask(1);
        taskManager.getSubtask(4);
        taskManager.getEpic(3);
        taskManager.getTask(2);


        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new HttpTasksHandler(taskManager));
        httpServer.createContext("/subtasks", new HttpSubtasksHandler(taskManager));
        httpServer.createContext("/epics", new HttpEpicsHandler(taskManager));
        httpServer.createContext("/history", new HttpHistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new HttpPrioritizedHandler(taskManager));
        httpServer.start();
    }
}
