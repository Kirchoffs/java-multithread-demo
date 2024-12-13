package org.syh.demo.java.multithreading.others;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TimeoutConditionThreadDemo {
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public void performTaskWithTimeout(long timeout, TimeUnit unit) throws TimeoutException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit(() -> {
            try {
                lock.lock();
                try {
                    while (!isConditionSatisfied()) {
                        condition.await();
                    }
                    performTask();
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread was interrupted: " + e.getMessage());
            }
        });

        try {
            future.get(timeout, unit);
        } catch (TimeoutException e) {
            future.cancel(true);
            throw new TimeoutException("Task timed out and was interrupted.");
        } catch (ExecutionException e) {
            throw new RuntimeException("Unexpected error occurred during execution", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Main thread was interrupted", e);
        } finally {
            executor.shutdownNow();
        }
    }

    private boolean isConditionSatisfied() {
        return false;
    }

    private void performTask() {
        System.out.println("Condition satisfied. Task executed.");
    }

    public static void main(String[] args) {
        TimeoutConditionThreadDemo example = new TimeoutConditionThreadDemo();
        try {
            example.performTaskWithTimeout(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            System.err.println("TimeoutException: " + e.getMessage());
        }
    }
}
