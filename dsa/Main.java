import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.nio.file.Paths;
import handlers.*;
import store.TaskStore;

public class Main {
    private static final TaskStore store = new TaskStore();

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        AuthHandler auth = new AuthHandler();

        // API routes
        server.createContext("/api/tasks", new TasksHandler(store));
        server.createContext("/api/login", auth);
        server.createContext("/api/register", auth);

        String fwdRoot = "fwd";
        StaticHandler staticHandler = new StaticHandler(fwdRoot);
        server.createContext("/fwd", staticHandler);
        server.createContext("/", staticHandler);
        
        StaticHandler staticHandler = new StaticHandler(fwdRoot);
        server.createContext("/fwd", staticHandler);
        server.createContext("/", staticHandler);

        server.setExecutor(null);
        System.out.println("Server running at http://localhost:8080");
        System.out.println("Frontend at  http://localhost:8080/");
        server.start();
    }
}
