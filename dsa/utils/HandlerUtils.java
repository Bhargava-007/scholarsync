package utils;

import models.Task;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class HandlerUtils {
    public static boolean handleOptions(HttpExchange ex) throws IOException {
        addCors(ex);
        if ("OPTIONS".equals(ex.getRequestMethod())) {
            ex.sendResponseHeaders(204, -1);
            ex.close();
            return true;
        }
        return false;
    }

    public static String readBody(HttpExchange ex) throws IOException {
        InputStream in = ex.getRequestBody();
        return new String(in.readAllBytes(), StandardCharsets.UTF_8);
    }

    public static void sendJson(HttpExchange ex, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        addCors(ex);
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        ex.sendResponseHeaders(status, bytes.length);
        OutputStream out = ex.getResponseBody();
        out.write(bytes);
        out.close();
    }

    public static void addCors(HttpExchange ex) {
        ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        ex.getResponseHeaders().set("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        ex.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
    }

    public static String queryParam(String query, String key) {
        if (query == null)
            return null;
        String[] pairs = query.split("&");
        for (String p : pairs) {
            String[] kv = p.split("=", 2);
            if (kv.length == 2 && kv[0].equals(key)) {
                return URLDecoder.decode(kv[1], StandardCharsets.UTF_8);
            }
        }
        return null;
    }

    public static String jsonValue(String body, String key) {
        String marker = "\"" + key + "\"";
        int m = body.indexOf(marker);
        if (m < 0)
            return null;
        int colon = body.indexOf(':', m + marker.length());
        if (colon < 0)
            return null;
        int i = colon + 1;
        while (i < body.length() && Character.isWhitespace(body.charAt(i)))
            i++;
        if (i >= body.length())
            return null;

        if (body.charAt(i) == '"') {
            int j = i + 1;
            StringBuilder s = new StringBuilder();
            while (j < body.length()) {
                char ch = body.charAt(j);
                if (ch == '"' && body.charAt(j - 1) != '\\')
                    break;
                s.append(ch);
                j++;
            }
            return s.toString();
        }

        int j = i;
        while (j < body.length() && body.charAt(j) != ',' && body.charAt(j) != '}')
            j++;
        return body.substring(i, j).trim();
    }

    public static String tasksJson(java.util.List<Task> tasks) {
        StringBuilder out = new StringBuilder("[");
        for (int i = 0; i < tasks.size(); i++) {
            if (i > 0)
                out.append(",");
            out.append(taskJson(tasks.get(i)));
        }
        out.append("]");
        return out.toString();
    }

    public static String taskJson(Task t) {
        return "{"
                + "\"id\":\"" + esc(t.id) + "\","
                + "\"title\":\"" + esc(t.title) + "\","
                + "\"subject\":\"" + esc(t.subject) + "\","
                + "\"priority\":" + t.priority + ","
                + "\"deadline\":\"" + esc(t.deadline) + "\","
                + "\"status\":\"" + esc(t.status) + "\""
                + "}";
    }

    public static String esc(String value) {
        if (value == null)
            return "";
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public static boolean blank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
