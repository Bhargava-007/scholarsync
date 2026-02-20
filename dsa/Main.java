import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import handlers.*;
import store.TaskStore;

public class Main {
    private static final TaskStore store = new TaskStore();

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        AuthHandler auth = new AuthHandler();
        server.createContext("/api/tasks", new TasksHandler(store));
        server.createContext("/api/login", auth);
        server.createContext("/api/register", auth);
        server.setExecutor(null);
        System.out.println("Server running at http://localhost:8080");
        server.start();
    }
}
