import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Minimal in-memory task storage used by Main.
public class TaskStore {
    private final List<Task> tasks = new ArrayList<>();

    public List<Task> all() {
        return tasks;
    }

    public Task find(String id) {
        for (Task t : tasks) {
            if (t.id.equals(id)) {
                return t;
            }
        }
        return null;
    }

    public boolean add(Task task) {
        if (find(task.id) != null) {
            return false;
        }
        tasks.add(task);
        return true;
    }

    public boolean delete(String id) {
        Task t = find(id);
        if (t == null) {
            return false;
        }
        tasks.remove(t);
        return true;
    }

    public boolean move(String id, String to) {
        Task t = find(id);
        if (t == null) {
            return false;
        }
        t.status = to;
        return true;
    }

    public List<Task> top(int k) {
        List<Task> copy = new ArrayList<>(tasks);
        copy.sort(
            Comparator.comparingInt((Task t) -> t.priority).reversed()
                .thenComparing(t -> t.deadline)
        );
        if (k < copy.size()) {
            return copy.subList(0, k);
        }
        return copy;
    }
}
