package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.TaskManager;

import java.io.IOException;

public class HttpHistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public HttpHistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equals("GET")) {
            Gson gson = makeGson();
            String historyJson = gson.toJson(taskManager.getHistory());
            sendText(exchange, historyJson);
        } else {
            sendMessage(exchange, "Такого эндпоинта не существует", 404);
        }
    }
}
