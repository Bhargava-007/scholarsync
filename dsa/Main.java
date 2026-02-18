import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

// Beginner-level HttpServer with only essential endpoints for the UI.
public class Main {
    private static final TaskStore store = new TaskStore();

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/tasks", new TasksHandler());
        server.setExecutor(null);
        System.out.println("Server running at http://localhost:8080");
        server.start();
    }

    static class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            if (handleOptions(ex)) {
                return;
            }

            String method = ex.getRequestMethod();
            String path = ex.getRequestURI().getPath();
            String query = ex.getRequestURI().getRawQuery();

            if ("GET".equals(method) && "/api/tasks".equals(path)) {
                sendJson(ex, 200, "{\"tasks\":" + tasksJson(store.all()) + "}");
                return;
            }

            if ("POST".equals(method) && "/api/tasks".equals(path)) {
                String body = readBody(ex);
                String id = jsonValue(body, "id");
                String title = jsonValue(body, "title");
                String subject = jsonValue(body, "subject");
                String priorityText = jsonValue(body, "priority");
                String deadline = jsonValue(body, "deadline");

                if (blank(id) || blank(title) || blank(subject) || blank(priorityText) || blank(deadline)) {
                    sendJson(ex, 400, "{\"error\":\"All fields are required\"}");
                    return;
                }

                int priority;
                try {
                    priority = Integer.parseInt(priorityText);
                } catch (Exception err) {
                    sendJson(ex, 400, "{\"error\":\"Priority must be a number\"}");
                    return;
                }

                Task task = new Task(id, title, subject, priority, deadline, "todo");
                if (!store.add(task)) {
                    sendJson(ex, 400, "{\"error\":\"Task id already exists\"}");
                    return;
                }

                sendJson(ex, 201, "{\"message\":\"Task added\"}");
                return;
            }

            if ("PUT".equals(method) && path.startsWith("/api/tasks/") && path.endsWith("/move")) {
                String[] parts = path.split("/");
                if (parts.length < 5) {
                    sendJson(ex, 400, "{\"error\":\"Invalid path\"}");
                    return;
                }
                String id = parts[3];
                String to = queryParam(query, "to");

                if (!("todo".equals(to) || "doing".equals(to) || "done".equals(to))) {
                    sendJson(ex, 400, "{\"error\":\"to must be todo|doing|done\"}");
                    return;
                }

                if (!store.move(id, to)) {
                    sendJson(ex, 404, "{\"error\":\"Task not found\"}");
                    return;
                }

                sendJson(ex, 200, "{\"message\":\"Task moved\"}");
                return;
            }

            if ("DELETE".equals(method) && path.startsWith("/api/tasks/")) {
                String[] parts = path.split("/");
                if (parts.length < 4) {
                    sendJson(ex, 400, "{\"error\":\"Invalid path\"}");
                    return;
                }
                String id = parts[3];
                if (!store.delete(id)) {
                    sendJson(ex, 404, "{\"error\":\"Task not found\"}");
                    return;
                }
                sendJson(ex, 200, "{\"message\":\"Task deleted\"}");
                return;
            }

            if ("GET".equals(method) && "/api/tasks/top".equals(path)) {
                String kText = queryParam(query, "k");
                int k = 3;
                try {
                    if (!blank(kText)) {
                        k = Integer.parseInt(kText);
                    }
                } catch (Exception ignored) {
                }
                List<Task> top = store.top(Math.max(1, k));
                sendJson(ex, 200, "{\"tasks\":" + tasksJson(top) + "}");
                return;
            }

            sendJson(ex, 404, "{\"error\":\"Not found\"}");
        }
    }

    private static boolean handleOptions(HttpExchange ex) throws IOException {
        addCors(ex);
        if ("OPTIONS".equals(ex.getRequestMethod())) {
            ex.sendResponseHeaders(204, -1);
            ex.close();
            return true;
        }
        return false;
    }

    private static String readBody(HttpExchange ex) throws IOException {
        InputStream in = ex.getRequestBody();
        return new String(in.readAllBytes(), StandardCharsets.UTF_8);
    }

    private static String tasksJson(List<Task> tasks) {
        StringBuilder out = new StringBuilder("[");
        for (int i = 0; i < tasks.size(); i++) {
            if (i > 0) {
                out.append(",");
            }
            out.append(taskJson(tasks.get(i)));
        }
        out.append("]");
        return out.toString();
    }

    private static String taskJson(Task t) {
        return "{"
            + "\"id\":\"" + esc(t.id) + "\","
            + "\"title\":\"" + esc(t.title) + "\","
            + "\"subject\":\"" + esc(t.subject) + "\","
            + "\"priority\":" + t.priority + ","
            + "\"deadline\":\"" + esc(t.deadline) + "\","
            + "\"status\":\"" + esc(t.status) + "\""
            + "}";
    }

    private static void sendJson(HttpExchange ex, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        addCors(ex);
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        ex.sendResponseHeaders(status, bytes.length);
        OutputStream out = ex.getResponseBody();
        out.write(bytes);
        out.close();
    }

    private static void addCors(HttpExchange ex) {
        ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        ex.getResponseHeaders().set("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        ex.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
    }

    private static String queryParam(String query, String key) {
        if (query == null) {
            return null;
        }
        String[] pairs = query.split("&");
        for (String p : pairs) {
            String[] kv = p.split("=", 2);
            if (kv.length == 2 && kv[0].equals(key)) {
                return URLDecoder.decode(kv[1], StandardCharsets.UTF_8);
            }
        }
        return null;
    }

    // Tiny parser for simple JSON request body.
    private static String jsonValue(String body, String key) {
        String marker = "\"" + key + "\"";
        int m = body.indexOf(marker);
        if (m < 0) {
            return null;
        }
        int colon = body.indexOf(':', m + marker.length());
        if (colon < 0) {
            return null;
        }
        int i = colon + 1;
        while (i < body.length() && Character.isWhitespace(body.charAt(i))) {
            i++;
        }
        if (i >= body.length()) {
            return null;
        }

        if (body.charAt(i) == '"') {
            int j = i + 1;
            StringBuilder s = new StringBuilder();
            while (j < body.length()) {
                char ch = body.charAt(j);
                if (ch == '"' && body.charAt(j - 1) != '\\') {
                    break;
                }
                s.append(ch);
                j++;
            }
            return s.toString();
        }

        int j = i;
        while (j < body.length() && body.charAt(j) != ',' && body.charAt(j) != '}') {
            j++;
        }
        return body.substring(i, j).trim();
    }

    private static String esc(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static boolean blank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
