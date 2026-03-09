package store;

import models.Task;
import co1algorithms.SearchAlgorithms;
import co1algorithms.SortingAlgorithms;
import co2adt.TaskLinkedList;
import co3stacksqueues.TaskPriorityHeap;
import java.io.*;
import java.util.Arrays;
import java.util.List;

public class TaskStore {
    private TaskLinkedList tasks = new TaskLinkedList();
    private static final String FILE_PATH = "tasks.txt";

    public TaskStore() {
        loadFromFile();
    }

    public List<Task> all() {
        return Arrays.asList(tasks.toArray());
    }

    public Task find(String id) {
        return SearchAlgorithms.linearSearchById(tasks.toArray(), id);
    }

    public boolean add(Task task) {
        if (tasks.find(task.id) != null) {
            return false;
        }
        tasks.addBack(task);
        saveToFile();
        return true;
    }

    public boolean delete(String id) {
        if (!tasks.delete(id)) {
            return false;
        }
        saveToFile();
        return true;
    }

    public boolean move(String id, String to) {
        if (!tasks.updateStatus(id, to)) {
            return false;
        }
        saveToFile();
        return true;
    }

    public List<Task> top(int k) {
        if (tasks.isEmpty())
            return Arrays.asList();
        Task[] allTasks = tasks.toArray();
        TaskPriorityHeap heap = new TaskPriorityHeap(allTasks.length);
        for (Task t : allTasks) {
            heap.insert(t);
        }
        return Arrays.asList(heap.topK(Math.max(1, k)));
    }

    public List<Task> sortedByPriority() {
        Task[] allTasks = tasks.toArray();
        SortingAlgorithms.mergeSort(allTasks, 0, allTasks.length - 1);
        return Arrays.asList(allTasks);
    }

    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Task t : tasks.toArray()) {
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
                    tasks.addBack(new Task(p[0], p[1], p[2], Integer.parseInt(p[3]), p[4], p[5]));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
        }
    }
}
