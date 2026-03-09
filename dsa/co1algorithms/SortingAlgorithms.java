package co1algorithms;

import models.Task;
import java.util.Arrays;

public class SortingAlgorithms {
    public static void mergeSort(Task[] arr, int left, int right) {
        if (left >= right)
            return;
        int mid = left + (right - left) / 2;
        mergeSort(arr, left, mid);
        mergeSort(arr, mid + 1, right);
        merge(arr, left, mid, right);
    }

    private static void merge(Task[] arr, int left, int mid, int right) {
        Task[] leftArr = Arrays.copyOfRange(arr, left, mid + 1);
        Task[] rightArr = Arrays.copyOfRange(arr, mid + 1, right + 1);
        int i = 0, j = 0, k = left;
        while (i < leftArr.length && j < rightArr.length) {
            if (compareByPriorityDesc(leftArr[i], rightArr[j]) >= 0) {
                arr[k++] = leftArr[i++];
            } else {
                arr[k++] = rightArr[j++];
            }
        }
        while (i < leftArr.length)
            arr[k++] = leftArr[i++];
        while (j < rightArr.length)
            arr[k++] = rightArr[j++];
    }

    private static int compareByPriorityDesc(Task a, Task b) {
        if (a.priority != b.priority)
            return Integer.compare(a.priority, b.priority);
        return b.deadline.compareTo(a.deadline);
    }

}
