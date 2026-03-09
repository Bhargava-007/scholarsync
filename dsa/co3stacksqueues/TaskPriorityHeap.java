package co3stacksqueues;
import models.Task;
public class TaskPriorityHeap {
    private Task[] heap;
    private int size;
    public TaskPriorityHeap(int initialCapacity) {
        heap = new Task[initialCapacity];
        size = 0;
    }
    public void insert(Task task) {
        ensureCapacity();
        heap[size] = task;
        bubbleUp(size);
        size++;
    }
    private void bubbleUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (compare(heap[i], heap[parent]) > 0) {
                swap(i, parent);
                i = parent;
            } else {
                break;
            }
        }
    }
    public Task poll() {
        if (size == 0)
            return null;
        Task top = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        if (size > 0)
            bubbleDown(0);
        return top;
    }
    private void bubbleDown(int i) {
        while (true) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int largest = i;
            if (left < size && compare(heap[left], heap[largest]) > 0)
                largest = left;
            if (right < size && compare(heap[right], heap[largest]) > 0)
                largest = right;
            if (largest == i)
                break;
            swap(i, largest);
            i = largest;
        }
    }
    public Task peek() {
        return size == 0 ? null : heap[0];
    }
    public Task[] topK(int k) {
        TaskPriorityHeap copy = new TaskPriorityHeap(size + 1);
        for (int i = 0; i < size; i++)
            copy.insert(heap[i]);
        int take = Math.min(k, size);
        Task[] result = new Task[take];
        for (int i = 0; i < take; i++) {
            result[i] = copy.poll();
        }
        return result;
    }
    private int compare(Task a, Task b) {
        if (a.priority != b.priority)
            return Integer.compare(a.priority, b.priority);
        return b.deadline.compareTo(a.deadline);
    }
    private void swap(int i, int j) {
        Task tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }
    private void ensureCapacity() {
        if (size == heap.length) {
            Task[] newHeap = new Task[heap.length * 2];
            System.arraycopy(heap, 0, newHeap, 0, size);
            heap = newHeap;
        }
    }
    public int size() {
        return size;
    }
    public boolean isEmpty() {
        return size == 0;
    }
}
