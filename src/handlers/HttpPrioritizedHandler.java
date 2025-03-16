package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.TaskManager;

import java.io.IOException;

public class HttpPrioritizedHandler extends BaseHttpHandler implements HttpHandler {

    public HttpPrioritizedHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equals("GET")) {
            String prioritizedTasksJson = gson.toJson(taskManager.getPrioritizedTasks());
            sendText(exchange, prioritizedTasksJson);
        } else {
            sendMessage(exchange, "Такого эндпоинта не существует", 404);
        }
    }
}
