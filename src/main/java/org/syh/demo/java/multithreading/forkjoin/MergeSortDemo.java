package org.syh.demo.java.multithreading.forkjoin;

import java.util.concurrent.CountedCompleter;
import java.util.concurrent.ForkJoinPool;

public class MergeSortDemo extends CountedCompleter<Void> {
    private final int[] arr;
    private final int lo, hi;

    public MergeSortDemo(MergeSortDemo parent, int[] arr, int lo, int hi) {
        super(parent);
        this.arr = arr;
        this.lo = lo;
        this.hi = hi;
    }

    public MergeSortDemo(int[] arr) {
        this(null, arr, 0, arr.length);
    }

    @Override
    public void compute() {
        if (hi - lo > 2) {
            int mid = lo + (hi - lo) / 2;
            addToPendingCount(2);
            new MergeSortDemo(this, arr, lo, mid).fork();
            new MergeSortDemo(this, arr, mid, hi).fork();
        } else if (hi - lo == 2) {
            if (arr[lo] > arr[lo + 1]) {
                int temp = arr[lo];
                arr[lo] = arr[lo + 1];
                arr[lo + 1] = temp;
            }
        }
        tryComplete();
    }

    @Override
    public void onCompletion(CountedCompleter<?> caller) {
        if (hi - lo > 2) {
            int mid = lo + (hi - lo) / 2;
            merge(arr, lo, mid, hi);
        }
    }

    private void merge(int[] arr, int lo, int mid, int hi) {
        int[] aux = new int[hi - lo];
        int i = lo, j = mid, k = 0;
        while (i < mid && j < hi) {
            aux[k++] = arr[i] < arr[j] ? arr[i++] : arr[j++];
        }
        while (i < mid) {
            aux[k++] = arr[i++];
        }
        while (j < hi) {
            aux[k++] = arr[j++];
        }
        System.arraycopy(aux, 0, arr, lo, aux.length);
    }

    public static void main(String[] args) {
        int[] arr = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5};
        MergeSortDemo completer = new MergeSortDemo(arr);
        ForkJoinPool.commonPool().invoke(completer);
        System.out.println(java.util.Arrays.toString(arr));
    }
}
