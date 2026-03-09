package co1algorithms;

import models.Task;

public class SearchAlgorithms {
    public static Task linearSearchById(Task[] arr, String id) {
        for (Task t : arr) {
            if (t.id.equals(id))
                return t;
        }
        return null;
    }
}
