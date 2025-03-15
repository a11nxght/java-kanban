package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.TaskManager;

import java.io.IOException;

public class HttpPrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;


    public HttpPrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equals("GET")) {
            Gson gson = makeGson();
            String prioritizedTasksJson = gson.toJson(taskManager.getPrioritizedTasks());
            sendText(exchange, prioritizedTasksJson);
        } else {
            sendMessage(exchange, "Такого эндпоинта не существует", 404);
        }
    }
}
