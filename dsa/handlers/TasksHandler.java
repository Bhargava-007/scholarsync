package handlers;

import models.Task;
import store.TaskStore;
import utils.HandlerUtils;
import utils.AppException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.List;

public class TasksHandler implements HttpHandler {
    private final TaskStore store;

    public TasksHandler(TaskStore store) {
        this.store = store;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (HandlerUtils.handleOptions(ex)) {
            return;
        }

        try {
            String method = ex.getRequestMethod();
            String path = ex.getRequestURI().getPath();
            String query = ex.getRequestURI().getRawQuery();

            if ("GET".equals(method) && "/api/tasks".equals(path)) {
                HandlerUtils.sendJson(ex, 200, "{\"tasks\":" + HandlerUtils.tasksJson(store.all()) + "}");
                return;
            }

            if ("POST".equals(method) && "/api/tasks".equals(path)) {
                String body = HandlerUtils.readBody(ex);
                String id = HandlerUtils.jsonValue(body, "id");
                String title = HandlerUtils.jsonValue(body, "title");
                String subject = HandlerUtils.jsonValue(body, "subject");
                String priorityText = HandlerUtils.jsonValue(body, "priority");
                String deadline = HandlerUtils.jsonValue(body, "deadline");

                if (HandlerUtils.blank(id) || HandlerUtils.blank(title) || HandlerUtils.blank(subject)
                        || HandlerUtils.blank(priorityText) || HandlerUtils.blank(deadline)) {
                    throw new AppException("All fields are required", 400);
                }

                int priority;
                try {
                    priority = Integer.parseInt(priorityText);
                } catch (Exception err) {
                    throw new AppException("Priority must be a number", 400);
                }

                Task task = new Task(id, title, subject, priority, deadline, "todo");
                if (!store.add(task)) {
                    throw new AppException("Task id already exists", 400);
                }

                HandlerUtils.sendJson(ex, 201, "{\"message\":\"Task added\"}");
                return;
            }

            if ("PUT".equals(method) && path.startsWith("/api/tasks/") && path.endsWith("/move")) {
                String[] parts = path.split("/");
                if (parts.length < 5) {
                    throw new AppException("Invalid path", 400);
                }
                String id = parts[3];
                String to = HandlerUtils.queryParam(query, "to");

                if (!("todo".equals(to) || "doing".equals(to) || "done".equals(to))) {
                    throw new AppException("to must be todo|doing|done", 400);
                }

                if (!store.move(id, to)) {
                    throw new AppException("Task not found", 404);
                }

                HandlerUtils.sendJson(ex, 200, "{\"message\":\"Task moved\"}");
                return;
            }

            if ("DELETE".equals(method) && path.startsWith("/api/tasks/")) {
                String[] parts = path.split("/");
                if (parts.length < 4) {
                    throw new AppException("Invalid path", 400);
                }
                String id = parts[3];
                if (!store.delete(id)) {
                    throw new AppException("Task not found", 404);
                }
                HandlerUtils.sendJson(ex, 200, "{\"message\":\"Task deleted\"}");
                return;
            }

            if ("GET".equals(method) && "/api/tasks/top".equals(path)) {
                String kText = HandlerUtils.queryParam(query, "k");
                int k = 3;
                try {
                    if (!HandlerUtils.blank(kText)) {
                        k = Integer.parseInt(kText);
                    }
                } catch (Exception ignored) {
                }
                List<Task> top = store.top(Math.max(1, k));
                HandlerUtils.sendJson(ex, 200, "{\"tasks\":" + HandlerUtils.tasksJson(top) + "}");
                return;
            }

            HandlerUtils.sendJson(ex, 404, "{\"error\":\"Not found\"}");
        } catch (AppException e) {
            HandlerUtils.sendJson(ex, e.getStatus(), "{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            HandlerUtils.sendJson(ex, 500, "{\"error\":\"Internal server error\"}");
        }
    }
}
