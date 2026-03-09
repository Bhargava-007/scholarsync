package co2adt;

import models.Task;

public class TaskLinkedList {
    public static class Node {
        public Task task;
        public Node next;

        public Node(Task task) {
            this.task = task;
        }
    }

    private Node head;
    private int size;

    public void addFront(Task task) {
        Node node = new Node(task);
        node.next = head;
        head = node;
        size++;
    }

    public void addBack(Task task) {
        Node node = new Node(task);
        if (head == null) {
            head = node;
        } else {
            Node cur = head;
            while (cur.next != null)
                cur = cur.next;
            cur.next = node;
        }
        size++;
    }

    public boolean delete(String id) {
        if (head == null)
            return false;
        if (head.task.id.equals(id)) {
            head = head.next;
            size--;
            return true;
        }
        Node prev = head;
        Node cur = head.next;
        while (cur != null) {
            if (cur.task.id.equals(id)) {
                prev.next = cur.next;
                size--;
                return true;
            }
            prev = cur;
            cur = cur.next;
        }
        return false;
    }

    public Task find(String id) {
        Node cur = head;
        while (cur != null) {
            if (cur.task.id.equals(id))
                return cur.task;
            cur = cur.next;
        }
        return null;
    }

    public Task[] toArray() {
        Task[] arr = new Task[size];
        Node cur = head;
        int i = 0;
        while (cur != null) {
            arr[i++] = cur.task;
            cur = cur.next;
        }
        return arr;
    }

    public void reverse() {
        Node prev = null;
        Node cur = head;
        while (cur != null) {
            Node next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }
        head = prev;
    }

    public boolean detectCycle() {
        Node slow = head;
        Node fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast)
                return true;
        }
        return false;
    }

    public boolean updateStatus(String id, String newStatus) {
        Task t = find(id);
        if (t == null)
            return false;
        t.status = newStatus;
        return true;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Node getHead() {
        return head;
    }
}
