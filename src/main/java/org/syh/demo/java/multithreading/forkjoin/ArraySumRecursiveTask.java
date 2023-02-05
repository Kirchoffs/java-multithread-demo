package org.syh.demo.java.multithreading.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ArraySumRecursiveTask extends RecursiveTask<Long> {
    private static final int THRESHOLD = 100;
    private long[] numbers;
    private int start;
    private int end;

    public ArraySumRecursiveTask(long[] numbers, int start, int end) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        int length = end - start;
        if (length <= THRESHOLD) {
            return sumSequentially();
        }
        ArraySumRecursiveTask leftTask = new ArraySumRecursiveTask(numbers, start, start + length / 2);
        leftTask.fork();
        ArraySumRecursiveTask rightTask = new ArraySumRecursiveTask(numbers, start + length / 2, end);
        Long rightResult = rightTask.compute();
        Long leftResult = leftTask.join();
        return leftResult + rightResult;
    }

    private long sumSequentially() {
        long sum = 0;
        for (int i = start; i < end; i++) {
            sum += numbers[i];
        }
        return sum;
    }

    public static void main(String[] args) {
        final int length = 10000000;
        long[] numbers = new long[length];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = i;
        }

        ArraySumRecursiveTask task1 = new ArraySumRecursiveTask(numbers, 0, numbers.length);
        ForkJoinPool pool1 = new ForkJoinPool();
        long result1 = pool1.invoke(task1);
        System.out.println("Result1: " + result1);

        ArraySumRecursiveTask task2 = new ArraySumRecursiveTask(numbers, 0, numbers.length);
        ForkJoinPool pool2 = new ForkJoinPool();
        long result2 = pool2.submit(task2).join();
        System.out.println("Result2: " + result2);

        ArraySumRecursiveTask task3 = new ArraySumRecursiveTask(numbers, 0, numbers.length);
        ForkJoinPool pool3 = ForkJoinPool.commonPool();
        long result3 = pool3.invoke(task3);
        System.out.println("Result3: " + result3);

        ArraySumRecursiveTask task4 = new ArraySumRecursiveTask(numbers, 0, numbers.length);
        long result4 = task4.invoke();
        System.out.println("Result4: " + result4);

        ArraySumRecursiveTask task5 = new ArraySumRecursiveTask(numbers, 0, numbers.length);
        task5.fork();
        long result5 = task5.join();
        System.out.println("Result5: " + result5);
    }
}
