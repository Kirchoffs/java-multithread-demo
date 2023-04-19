package org.syh.demo.java.multithreading.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolFirstDemo {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 10; i++) {
            Runnable task = new Task(i);
            executor.submit(task);
        }

        executor.shutdown();
    }

    private static class Task implements Runnable {
        private final int id;

        public Task(int id) {
            this.id = id;
        }

        public void run() {
            System.out.println("Task " + id + " started");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Task " + id + " finished");
        }
    }
}
