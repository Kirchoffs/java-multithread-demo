package org.syh.demo.java.multithreading.others;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TimeoutLockDemo {
    public static void main(String[] args) throws InterruptedException {
        Lock lock = new ReentrantLock();

        new Thread(() -> {
            lock.lock();
            System.out.println("Thread 1 got lock");
            try {
                TimeUnit.SECONDS.sleep(6);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                System.out.println("Thread 1 released lock");
            }
        }).start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(() -> {
            boolean lockAcquired = false;
            try {
                System.out.println("Thread 2 trying to get lock");
                long startTime = System.currentTimeMillis();
                lockAcquired = lock.tryLock(2, TimeUnit.SECONDS);
                // lock.tryLock will not block the thread even if the lock is not acquired
                if (!lockAcquired) {
                    System.out.println("Thread 2 failed to get lock");
                    return;
                }

                long endTime = System.currentTimeMillis();
                System.out.println("Thread 2 got lock");
                System.out.println("Time elapsed: " + (endTime - startTime) / 1000 + "s");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (lockAcquired) {
                    lock.unlock();
                    System.out.println("Thread 2 released lock");
                } 
            }
        }).start();
    }
}
