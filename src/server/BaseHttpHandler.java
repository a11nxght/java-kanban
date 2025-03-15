package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

class BaseHttpHandler {
    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendNotFound(HttpExchange h) throws IOException {
        byte[] resp = "Некорректный ID".getBytes(StandardCharsets.UTF_8);
        h.sendResponseHeaders(404, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendMessage(HttpExchange h, String text, int rCode) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.sendResponseHeaders(rCode, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        if (pathParts.length == 2) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_ALL;
            }
            if (requestMethod.equals("POST")) {
                return Endpoint.POST;
            }
        }
        if (pathParts.length > 2) {
            switch (requestMethod) {
                case "GET" -> {
                    return Endpoint.GET_ID;
                }
                case "DELETE" -> {
                    return Endpoint.DELETE;
                }
            }
        }
        return Endpoint.UNKNOWN;
    }

    protected Gson makeGson() {
        return new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

    enum Endpoint {
        GET_ALL,
        GET_ID,
        POST,
        DELETE,
        UNKNOWN
    }
}