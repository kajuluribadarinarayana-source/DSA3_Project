import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import services.ClauseSearcher;
import services.ComplianceAnalyzer;
import services.DocumentComparator;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ApiServer {

    public static void main(String[] args)
            throws IOException {

        HttpServer server =
                HttpServer.create(
                        new InetSocketAddress(8080),
                        0
                );

        server.createContext(
                "/api/test",
                ApiServer::test
        );

        server.createContext(
                "/api/contracts",
                ApiServer::contracts
        );

        server.createContext(
                "/api/compare",
                ApiServer::compare
        );

        server.createContext(
                "/api/search",
                ApiServer::search
        );

        server.createContext(
                "/api/compliance",
                ApiServer::compliance
        );

        server.start();

        System.out.println(
                "===================================="
        );

        System.out.println(
                "Backend running at:"
        );

        System.out.println(
                "http://localhost:8080"
        );

        System.out.println(
                "===================================="
        );
    }

    private static void test(
            HttpExchange exchange)
            throws IOException {

        sendJson(
                exchange,
                "{\"message\":\"Backend is connected successfully!\"}"
        );
    }

    private static void contracts(
            HttpExchange exchange)
            throws IOException {

        List<String> contracts =
                ContractManager.getContracts();

        StringBuilder json =
                new StringBuilder("[ ");

        for (int i = 0;
             i < contracts.size();
             i++) {

            json.append("\"")
                    .append(
                            escapeJson(
                                    contracts.get(i)
                            )
                    )
                    .append("\"");

            if (i < contracts.size() - 1) {
                json.append(", ");
            }
        }

        json.append(" ]");

        sendJson(exchange, json.toString());
    }

    private static void compare(
            HttpExchange exchange)
            throws IOException {

        String query =
                exchange.getRequestURI()
                        .getRawQuery();

        String oldFile =
                getParameter(query, "old");

        String newFile =
                getParameter(query, "new");

        if (oldFile == null ||
                newFile == null) {

            sendJson(
                    exchange,
                    "{\"error\":\"Please provide old and new file names.\"}"
            );

            return;
        }

        Path oldPath =
                Path.of(
                        "../data/versions",
                        oldFile
                );

        Path newPath =
                Path.of(
                        "../data/versions",
                        newFile
                );

        String oldText =
                Files.readString(oldPath);

        String newText =
                Files.readString(newPath);

        String result =
                DocumentComparator.compare(
                        oldText,
                        newText
                );

        String json =
                "{\"result\":\""
                        + escapeJson(result)
                        + "\"}";

        sendJson(exchange, json);
    }

    private static void search(
            HttpExchange exchange)
            throws IOException {

        String query =
                exchange.getRequestURI()
                        .getRawQuery();

        String file =
                getParameter(query, "file");

        String keyword =
                getParameter(query, "keyword");

        if (file == null ||
                keyword == null) {

            sendJson(
                    exchange,
                    "{\"error\":\"file and keyword are required.\"}"
            );

            return;
        }

        String text =
                Files.readString(
                        Path.of(
                                "../data/versions",
                                file
                        )
                );

        String result =
                ClauseSearcher.search(
                        text,
                        keyword
                );

        sendJson(
                exchange,
                "{\"result\":\""
                        + escapeJson(result)
                        + "\"}"
        );
    }

    private static void compliance(
            HttpExchange exchange)
            throws IOException {

        String query =
                exchange.getRequestURI()
                        .getRawQuery();

        String file =
                getParameter(query, "file");

        if (file == null) {

            sendJson(
                    exchange,
                    "{\"error\":\"file is required.\"}"
            );

            return;
        }

        String text =
                Files.readString(
                        Path.of(
                                "../data/versions",
                                file
                        )
                );

        String result =
                ComplianceAnalyzer.analyze(text);

        sendJson(
                exchange,
                "{\"result\":\""
                        + escapeJson(result)
                        + "\"}"
        );
    }

    private static String getParameter(
            String query,
            String parameter) {

        if (query == null) {
            return null;
        }

        String[] pairs =
                query.split("&");

        for (String pair : pairs) {

            String[] keyValue =
                    pair.split("=", 2);

            if (keyValue.length == 2 &&
                    keyValue[0].equals(parameter)) {

                return URLDecoder.decode(
                        keyValue[1],
                        StandardCharsets.UTF_8
                );
            }
        }

        return null;
    }

    private static String escapeJson(
            String text) {

        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "")
                .replace("\n", "\\n");
    }

    private static void sendJson(
            HttpExchange exchange,
            String response)
            throws IOException {

        exchange.getResponseHeaders()
                .add(
                        "Content-Type",
                        "application/json"
                );

        exchange.getResponseHeaders()
                .add(
                        "Access-Control-Allow-Origin",
                        "*"
                );

        byte[] bytes =
                response.getBytes(
                        StandardCharsets.UTF_8
                );

        exchange.sendResponseHeaders(
                200,
                bytes.length
        );

        try (OutputStream output =
                     exchange.getResponseBody()) {

            output.write(bytes);
        }
    }
}