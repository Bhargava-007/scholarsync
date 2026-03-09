package co4hashingcollections;

import java.util.LinkedList;

public class UserStore {
    private static class Entry {
        String key;
        String value;

        Entry(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR_LIMIT = 0.75;
    private LinkedList<Entry>[] buckets;
    private int capacity;
    private int size;

    @SuppressWarnings("unchecked")
    public UserStore(int initialCapacity) {
        this.capacity = initialCapacity;
        this.buckets = new LinkedList[capacity];
        this.size = 0;
    }

    public UserStore() {
        this(DEFAULT_CAPACITY);
    }

    private int hash(String key) {
        return (key.hashCode() & 0x7FFFFFFF) % capacity;
    }

    public void put(String username, String password) {
        if ((double) size / capacity >= LOAD_FACTOR_LIMIT) {
            resize();
        }
        int index = hash(username);
        if (buckets[index] == null) {
            buckets[index] = new LinkedList<>();
        }
        for (Entry e : buckets[index]) {
            if (e.key.equals(username)) {
                e.value = password;
                return;
            }
        }
        buckets[index].add(new Entry(username, password));
        size++;
    }

    public String get(String username) {
        int index = hash(username);
        if (buckets[index] == null)
            return null;
        for (Entry e : buckets[index]) {
            if (e.key.equals(username))
                return e.value;
        }
        return null;
    }

    public boolean contains(String username) {
        return get(username) != null;
    }

    public boolean remove(String username) {
        int index = hash(username);
        if (buckets[index] == null)
            return false;
        return buckets[index].removeIf(e -> e.key.equals(username));
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = capacity * 2;
        LinkedList<Entry>[] newBuckets = new LinkedList[newCapacity];
        for (LinkedList<Entry> bucket : buckets) {
            if (bucket == null)
                continue;
            for (Entry e : bucket) {
                int newIndex = (e.key.hashCode() & 0x7FFFFFFF) % newCapacity;
                if (newBuckets[newIndex] == null)
                    newBuckets[newIndex] = new LinkedList<>();
                newBuckets[newIndex].add(e);
            }
        }
        buckets = newBuckets;
        capacity = newCapacity;
    }

    public void forEach(java.util.function.BiConsumer<String, String> action) {
        for (LinkedList<Entry> bucket : buckets) {
            if (bucket == null)
                continue;
            for (Entry e : bucket) {
                action.accept(e.key, e.value);
            }
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int capacity() {
        return capacity;
    }
}
