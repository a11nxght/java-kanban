package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.TaskManager;

import java.io.IOException;

public class HttpHistoryHandler extends BaseHttpHandler implements HttpHandler {

    public HttpHistoryHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equals("GET")) {
            String historyJson = gson.toJson(taskManager.getHistory());
            sendText(exchange, historyJson);
        } else {
            sendMessage(exchange, "Такого эндпоинта не существует", 404);
        }
    }
}
