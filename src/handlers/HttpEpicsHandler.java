package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.NotFoundException;
import service.TaskManager;
import tasks.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class HttpEpicsHandler extends BaseHttpHandler implements HttpHandler {

    public HttpEpicsHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        switch (endpoint) {
            case GET_ALL -> handleGetEpics(exchange);
            case GET_ID -> handleGetEpic(exchange);
            case POST -> handlePostCreateEpic(exchange);
            case DELETE -> handleDeleteEpic(exchange);
            case UNKNOWN -> sendMessage(exchange, "Такого эндпоинта не существует", 404);
        }
    }

    private void handleGetEpics(HttpExchange exchange) throws IOException {
        String epicsJson = gson.toJson(taskManager.getAllEpics());
        sendText(exchange, epicsJson);
    }

    private void handleGetEpic(HttpExchange exchange) throws IOException {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        if (pathParts.length == 3) {
            try {
                Epic epic = taskManager.getEpic(Integer.parseInt(pathParts[2]));
                String epicJson = gson.toJson(epic);
                sendText(exchange, epicJson);
            } catch (NumberFormatException | NotFoundException exception) {
                sendNotFound(exchange);
            }
        } else {
            if (pathParts[3].equals("subtasks")) {
                try {
                    int epicId = Integer.parseInt(pathParts[2]);
                    String epicSubtasksJson = gson.toJson(taskManager.getSubtasksFromEpic(epicId));
                    sendText(exchange, epicSubtasksJson);
                } catch (NumberFormatException | NotFoundException exception) {
                    sendNotFound(exchange);
                }
            } else {
                sendMessage(exchange, "Такого эндпоинта не существует", 404);
            }
        }
    }

    private void handlePostCreateEpic(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Epic epic = gson.fromJson(body, Epic.class);
        if (epic == null) {
            sendMessage(exchange, "Ошибка. Пустое тело запроса", 400);
            return;
        }
        if (epic.getDuration() == null) {
            epic.setDuration(Duration.ZERO);
        }
        taskManager.createNewEpic(epic);
        sendMessage(exchange, "Эпик добавлен", 201);
    }

    private void handleDeleteEpic(HttpExchange exchange) throws IOException {
        String pathPart = exchange.getRequestURI().getPath().split("/")[2];
        try {
            taskManager.deleteEpic(Integer.parseInt(pathPart));
            sendMessage(exchange, "Эпик удален", 200);
        } catch (NumberFormatException | NotFoundException exception) {
            sendNotFound(exchange);
        }
    }
}
