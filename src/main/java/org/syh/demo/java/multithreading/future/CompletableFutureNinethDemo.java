package org.syh.demo.java.multithreading.future;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CompletableFutureNinethDemo {
    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            futures.add(CompletableFuture.runAsync(new SleepRunnable(random.nextInt(5) + 1), executorService));
        }

        CompletableFuture.allOf(futures.stream().toArray(CompletableFuture[]::new)).get();
        System.out.println("All sleep tasks finished.");
    }
}

class SleepRunnable implements Runnable {
    private int sleepTime;
    public SleepRunnable(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public void run() {
        try {
            TimeUnit.SECONDS.sleep(sleepTime);
            System.out.println("Sleep " + sleepTime + " seconds.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
