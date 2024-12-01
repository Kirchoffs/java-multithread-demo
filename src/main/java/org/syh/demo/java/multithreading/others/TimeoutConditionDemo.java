package org.syh.demo.java.multithreading.others;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TimeoutConditionDemo {
    private static class ShareData {
        private final Lock lock = new ReentrantLock();
        private final Condition condition = lock.newCondition();

        public void awaitWithTimeout() {
            lock.lock();
            System.out.println("Await thread got lock");
            try {
                long startTime = System.currentTimeMillis();
                boolean success = condition.await(3, TimeUnit.SECONDS); // It cannot move on until it acquires the lock
                long endTime = System.currentTimeMillis();
                System.out.println("Time elapsed: " + (endTime - startTime) / 1000 + "s");
                if (!success) {
                    System.out.println("Timeout");
                }
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            } finally {
                lock.unlock();
                System.out.println("Await thread released lock");
            }
        }

        public void signal() {
            lock.lock();
            System.out.println("Signal thread got lock");
            try {
                TimeUnit.SECONDS.sleep(8);
                condition.signal();
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            } finally {
                lock.unlock();
                System.out.println("Signal thread released lock");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ShareData shareData = new ShareData();

        Thread t1 = new Thread(() -> {
            shareData.awaitWithTimeout();
        });

        Thread t2 = new Thread(() -> {
            shareData.signal();
        });

        t1.start();
        TimeUnit.SECONDS.sleep(1);
        t2.start();
    }
}
