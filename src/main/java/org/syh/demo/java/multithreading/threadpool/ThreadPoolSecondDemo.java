package org.syh.demo.java.multithreading.threadpool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadPoolSecondDemo {
    public static void main(String[] args) {
        int taskNumber = 40;
        int loopNumber = 10000;
        int threadNumber = 20;

        ExecutorService executor = Executors.newFixedThreadPool(threadNumber);
        List<Integer> integerList = Collections.synchronizedList(new ArrayList<>());;
        List<Future> futureList = new ArrayList<>();
        for (int i = 0; i < taskNumber; i++) {
            futureList.add(executor.submit(new Task(i, loopNumber, integerList)));
        }

        for (Future future : futureList) {
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("All tasks finished");
        System.out.println("List size: " + integerList.size());
        System.out.println("Expected size: " + taskNumber * loopNumber);
    }
}

class Task implements Runnable {
    private static final int MAX = 10000;
    private final int id;
    private final int loopNumber;
    private final List<Integer> list;

    public Task(int id, int loopNumber, List<Integer> list) {
        this.id = id;
        this.loopNumber = loopNumber;
        this.list = list;
    }

    public void run() {
        System.out.println("Task " + id + " started");
        for (int i = 0; i < loopNumber; i++) {
            list.add(new Random().nextInt(MAX));
        }
        System.out.println("Task " + id + " finished");
    }
}
