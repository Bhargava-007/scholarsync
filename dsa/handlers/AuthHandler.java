package handlers;

import utils.HandlerUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class AuthHandler implements HttpHandler {
    private static final Map<String, String> users = new HashMap<>();
    private static final String USER_FILE = "users.txt";

    static {
        loadUsers();
        if (users.isEmpty()) {
            users.put("admin", "password123");
            saveUsers();
        }
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (HandlerUtils.handleOptions(ex)) {
            return;
        }

        String method = ex.getRequestMethod();
        String path = ex.getRequestURI().getPath();

        if ("POST".equals(method)) {
            String body = HandlerUtils.readBody(ex);
            String user = HandlerUtils.jsonValue(body, "username");
            String pass = HandlerUtils.jsonValue(body, "password");

            if (user == null || pass == null || user.trim().isEmpty() || pass.trim().isEmpty()) {
                HandlerUtils.sendJson(ex, 400, "{\"success\":false, \"error\":\"Username and password required\"}");
                return;
            }

            if ("/api/login".equals(path)) {
                if (users.containsKey(user) && users.get(user).equals(pass)) {
                    HandlerUtils.sendJson(ex, 200, "{\"success\":true, \"message\":\"Login successful\"}");
                } else {
                    HandlerUtils.sendJson(ex, 401, "{\"success\":false, \"error\":\"Invalid username or password\"}");
                }
            } else if ("/api/register".equals(path)) {
                if (users.containsKey(user)) {
                    HandlerUtils.sendJson(ex, 400, "{\"success\":false, \"error\":\"User already exists\"}");
                } else {
                    users.put(user, pass);
                    saveUsers();
                    HandlerUtils.sendJson(ex, 201, "{\"success\":true, \"message\":\"User registered\"}");
                }
            }
        } else {
            HandlerUtils.sendJson(ex, 405, "{\"error\":\"Method not allowed\"}");
        }
    }

    private static void loadUsers() {
        File file = new File(USER_FILE);
        if (!file.exists())
            return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    users.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }

    private static void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (Map.Entry<String, String> entry : users.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }
}
