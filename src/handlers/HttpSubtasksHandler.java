package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.NotFoundException;
import exceptions.TasksOverlapException;
import service.TaskManager;
import tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class HttpSubtasksHandler extends BaseHttpHandler implements HttpHandler {


    public HttpSubtasksHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        switch (endpoint) {
            case GET_ALL -> handleGetSubtasks(exchange);
            case GET_ID -> handleGetSubtask(exchange);
            case POST -> handlePostCreateSubtask(exchange);
            case DELETE -> handleDeleteSubtask(exchange);
            case UNKNOWN -> sendMessage(exchange, "Такого эндпоинта не существует", 404);
        }
    }

    private void handleGetSubtasks(HttpExchange exchange) throws IOException {
        String subtasksJson = gson.toJson(taskManager.getAllSubtasks());
        sendText(exchange, subtasksJson);
    }

    private void handleGetSubtask(HttpExchange exchange) throws IOException {
        String pathPart = exchange.getRequestURI().getPath().split("/")[2];
        try {
            Subtask subtask = taskManager.getSubtask(Integer.parseInt(pathPart));
            String subtaskJson = gson.toJson(subtask);
            sendText(exchange, subtaskJson);
        } catch (NumberFormatException | NotFoundException exception) {
            sendNotFound(exchange);
        }
    }

    private void handlePostCreateSubtask(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Subtask subtask = gson.fromJson(body, Subtask.class);
        if (subtask == null) {
            sendMessage(exchange, "Ошибка. Пустое тело запроса", 400);
            return;
        }
        if (subtask.getDuration() == null) {
            subtask.setDuration(Duration.ZERO);
        }
        if (subtask.getTaskId() == 0) {
            try {
                taskManager.createNewSubtask(subtask);
                sendMessage(exchange, "Подзадача добавлена", 201);
            } catch (NotFoundException exception) {
                sendMessage(exchange, exception.getMessage(), 404);
            } catch (TasksOverlapException exception) {
                sendMessage(exchange, exception.getMessage(), 406);
            }
        } else {
            try {
                taskManager.updateSubtask(subtask);
                sendMessage(exchange, "Подзадача обновлена", 201);
            } catch (NotFoundException exception) {
                sendMessage(exchange, exception.getMessage(), 404);
            } catch (TasksOverlapException exception) {
                sendMessage(exchange, exception.getMessage(), 406);
            }
        }
    }

    private void handleDeleteSubtask(HttpExchange exchange) throws IOException {
        String pathPart = exchange.getRequestURI().getPath().split("/")[2];
        try {
            taskManager.deleteSubtask(Integer.parseInt(pathPart));
            sendMessage(exchange, "Подзадача удалена", 200);
        } catch (NumberFormatException | NotFoundException exception) {
            sendNotFound(exchange);
        }
    }

}
