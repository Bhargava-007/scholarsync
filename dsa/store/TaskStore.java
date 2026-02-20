package store;

import models.Task;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskStore {
    private List<Task> tasks = new ArrayList<>();
    private static final String FILE_PATH = "tasks.txt";

    public TaskStore() {
        loadFromFile();
    }

    public List<Task> all() {
        return tasks;
    }

    public Task find(String id) {
        return tasks.stream()
                .filter(t -> t.id.equals(id))
                .findFirst()
                .orElse(null);
    }

    public boolean add(Task task) {
        if (find(task.id) != null) {
            return false;
        }
        tasks.add(task);
        saveToFile();
        return true;
    }

    public boolean delete(String id) {
        Task t = find(id);
        if (t == null) {
            return false;
        }
        tasks.remove(t);
        saveToFile();
        return true;
    }

    public boolean move(String id, String to) {
        Task t = find(id);
        if (t == null) {
            return false;
        }
        t.status = to;
        saveToFile();
        return true;
    }

    public List<Task> top(int k) {
        return tasks.stream()
                .sorted((t1, t2) -> {
                    if (t1.priority != t2.priority) {
                        return Integer.compare(t2.priority, t1.priority);
                    }
                    return t1.deadline.compareTo(t2.deadline);
                })
                .limit(k)
                .collect(Collectors.toList());
    }

    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Task t : tasks) {
                writer.write(
                        t.id + "|" + t.title + "|" + t.subject + "|" + t.priority + "|" + t.deadline + "|" + t.status);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists())
            return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = line.split("\\|");
                if (p.length == 6) {
                    tasks.add(new Task(p[0], p[1], p[2], Integer.parseInt(p[3]), p[4], p[5]));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
        }
    }
}
