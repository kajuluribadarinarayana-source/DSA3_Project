
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {

    static class Contract {
        int id;
        String name;
        List<String> versions = new ArrayList<>();

        Contract(int id, String name, String text) {
            this.id = id;
            this.name = name;
            versions.add(text);
        }
    }

    static List<Contract> contracts = new ArrayList<>();
    static int nextId = 1;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(
                new InetSocketAddress(8080), 0);

        server.createContext("/api/status", exchange -> {
            send(exchange, 200,
                    "{\"message\":\"Backend connected successfully!\"}");
        });

        server.createContext("/api/contracts", exchange -> {
            try {
                addCors(exchange);

                if ("OPTIONS".equalsIgnoreCase(
                        exchange.getRequestMethod())) {
                    exchange.sendResponseHeaders(204, -1);
                    return;
                }

                if ("GET".equalsIgnoreCase(
                        exchange.getRequestMethod())) {
                    send(exchange, 200, listContracts());
                    return;
                }

                if ("POST".equalsIgnoreCase(
                        exchange.getRequestMethod())) {
                    String body = new String(
                            exchange.getRequestBody().readAllBytes(),
                            StandardCharsets.UTF_8);

                    Map<String, String> fields = parseJson(body);
                    String name = fields.get("name");
                    String text = fields.get("text");

                    if (name == null || name.isBlank()
                            || text == null || text.isBlank()) {
                        send(exchange, 400,
                                "{\"error\":\"Name and text are required\"}");
                        return;
                    }

                    Contract contract =
                            new Contract(nextId++, name, text);
                    contracts.add(contract);

                    send(exchange, 201, contractJson(contract));
                    return;
                }

                send(exchange, 405,
                        "{\"error\":\"Method not allowed\"}");

            } catch (Exception e) {
                send(exchange, 500,
                        "{\"error\":\"Request failed\"}");
            }
        });

        server.start();
        System.out.println(
                "Backend running at http://localhost:8080");
    }

    static String listContracts() {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < contracts.size(); i++) {
            if (i > 0) result.append(",");
            result.append(contractJson(contracts.get(i)));
        }
        return result.append("]").toString();
    }

    static String contractJson(Contract c) {
        StringBuilder result = new StringBuilder();
        result.append("{\"id\":").append(c.id)
              .append(",\"name\":\"").append(escape(c.name))
              .append("\",\"versions\":").append(c.versions.size())
              .append("}");
        return result.toString();
    }

    static Map<String, String> parseJson(String json) {
        Map<String, String> result = new HashMap<>();
        java.util.regex.Matcher m =
            java.util.regex.Pattern.compile(
                "\"(name|text)\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"")
            .matcher(json);

        while (m.find()) {
            result.put(m.group(1), unescape(m.group(2)));
        }
        return result;
    }

    static String escape(String value) {
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r");
    }

    static String unescape(String value) {
        return value.replace("\\n", "\n")
                    .replace("\\r", "\r")
                    .replace("\\\"", "\"")
                    .replace("\\\\", "\\");
    }

    static void addCors(HttpExchange exchange) {
        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Headers", "Content-Type");
    }

    static void send(HttpExchange exchange, int status, String body)
            throws IOException {
        addCors(exchange);
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set(
                "Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(bytes);
        }
    }
}
