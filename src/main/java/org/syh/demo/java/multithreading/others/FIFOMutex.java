package org.syh.demo.java.multithreading.others;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/*
 * A sketch of first-in-first-out non-reentrant lock class
 */
public class FIFOMutex {
    private final AtomicBoolean locked = new AtomicBoolean(false);
    private final Queue<Thread> waiters = new ConcurrentLinkedQueue<Thread>();
 
    public void lock() {
        boolean wasInterrupted = false;
        Thread current = Thread.currentThread();
        waiters.add(current);
    
        while (waiters.peek() != current || !locked.compareAndSet(false, true)) {
            LockSupport.park(this);
            if (Thread.interrupted()) {
                wasInterrupted = true;
            }
        }
    
        waiters.remove();
        if (wasInterrupted) {
            current.interrupt();
        }
    }
 
    public void unlock() {
        locked.set(false);
        LockSupport.unpark(waiters.peek());
    }
}

class FIFOMutexExample {
    private static final FIFOMutex mutex = new FIFOMutex();
    private static int counter = 0;

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            mutex.lock();
            try {
                for (int i = 0; i < 5; i++) {
                    counter++;
                    System.out.println("Thread 1: Counter = " + counter);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                mutex.unlock();
            }
        });

        Thread thread2 = new Thread(() -> {
            mutex.lock();
            try {
                for (int i = 0; i < 5; i++) {
                    counter++;
                    System.out.println("Thread 2: Counter = " + counter);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                mutex.unlock();
            }
        });

        thread1.start();
        thread2.start();
    }
}
