package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.nio.file.*;
import java.util.Map;

public class StaticHandler implements HttpHandler {
    private final Path root;

    private static final Map<String, String> MIME = Map.of(
            ".html", "text/html; charset=utf-8",
            ".css", "text/css; charset=utf-8",
            ".js", "application/javascript; charset=utf-8",
            ".png", "image/png",
            ".jpg", "image/jpeg",
            ".jpeg", "image/jpeg",
            ".ico", "image/x-icon",
            ".svg", "image/svg+xml");

    public StaticHandler(String rootDir) {
        this.root = Paths.get(rootDir).toAbsolutePath().normalize();
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");

        String uriPath = ex.getRequestURI().getPath();

        // Strip the leading /fwd prefix so requests map to the fwd/ folder
        if (uriPath.startsWith("/fwd")) {
            uriPath = uriPath.substring(4);
        }
        if (uriPath.isEmpty() || uriPath.equals("/")) {
            uriPath = "/index.html";
        }

        Path target = root.resolve(uriPath.replaceFirst("^/", "")).normalize();

        // Security: ensure the resolved path is still inside the root
        if (!target.startsWith(root)) {
            send(ex, 403, "text/plain", "Forbidden".getBytes());
            return;
        }

        // Serve a directory as its index.html
        if (Files.isDirectory(target)) {
            target = target.resolve("index.html");
        }

        if (!Files.exists(target)) {
            send(ex, 404, "text/plain", "Not Found".getBytes());
            return;
        }

        String fileName = target.getFileName().toString();
        int dot = fileName.lastIndexOf('.');
        String ext = dot >= 0 ? fileName.substring(dot) : "";
        String mime = MIME.getOrDefault(ext, "application/octet-stream");

        byte[] bytes = Files.readAllBytes(target);
        send(ex, 200, mime, bytes);
    }

    private void send(HttpExchange ex, int status, String mime, byte[] body) throws IOException {
        ex.getResponseHeaders().set("Content-Type", mime);
        ex.sendResponseHeaders(status, body.length);
        try (OutputStream out = ex.getResponseBody()) {
            out.write(body);
        }
    }
}
