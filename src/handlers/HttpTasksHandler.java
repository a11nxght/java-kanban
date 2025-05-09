package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.NotFoundException;
import exceptions.TasksOverlapException;
import service.TaskManager;
import tasks.Task;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;


public class HttpTasksHandler extends BaseHttpHandler implements HttpHandler {

    public HttpTasksHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        switch (endpoint) {
            case GET_ALL -> handleGetTasks(exchange);
            case GET_ID -> handleGetTask(exchange);
            case POST -> handlePostCreateTask(exchange);
            case DELETE -> handleDeleteTask(exchange);
            case UNKNOWN -> sendMessage(exchange, "Такого эндпоинта не существует", 404);
        }
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        String tasksJson = gson.toJson(taskManager.getAllTasks());
        sendText(exchange, tasksJson);
    }

    private void handleGetTask(HttpExchange exchange) throws IOException {
        String pathPart = exchange.getRequestURI().getPath().split("/")[2];
        try {
            Task task = taskManager.getTask(Integer.parseInt(pathPart));
            String taskJson = gson.toJson(task);
            sendText(exchange, taskJson);
        } catch (NumberFormatException | NotFoundException e) {
            sendNotFound(exchange);
        }
    }

    private void handlePostCreateTask(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Task task;
        task = gson.fromJson(body, Task.class);
        if (task == null) {
            sendMessage(exchange, "Ошибка. Пустое тело запроса", 400);
            return;
        }
        if (task.getDuration() == null) {
            task.setDuration(Duration.ZERO);
        }
        if (task.getTaskId() == 0) {

            int id = taskManager.createNewTask(task);
            if (id == 0) {
                sendMessage(exchange, "Задача пересекается с уже существующими", 406);
            } else {
                sendMessage(exchange, "Задача добавлена", 201);
            }
        } else {
            try {
                taskManager.updateTask(task);
                sendMessage(exchange, "Задача обновлена", 201);
            } catch (TasksOverlapException exception) {
                sendMessage(exchange, exception.getMessage(), 406);
            } catch (NotFoundException exception) {
                sendMessage(exchange, exception.getMessage(), 404);
            }
        }
    }

    private void handleDeleteTask(HttpExchange exchange) throws IOException {
        String pathPart = exchange.getRequestURI().getPath().split("/")[2];
        try {
            taskManager.deleteTask(Integer.parseInt(pathPart));
            sendMessage(exchange, "Задача удалена", 200);
        } catch (NumberFormatException | NotFoundException exception) {
            sendNotFound(exchange);
        }
    }
}

