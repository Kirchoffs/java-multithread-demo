package org.syh.demo.java.multithreading.others;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Channel<T> {
    private final Queue<T> queue = new LinkedList<>();
    private final Lock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();

    // The methods on a Condition object, such as await(), signal(), and signalAll(), must be called while holding the associated lock.
    // wait() method releases the lock and puts the current thread into a waiting state. The thread will remain in this state until it is signaled (via signal() or signalAll()) and can reacquire the lock.
    // signal() (or signalAll()) method wake up one of the threads (or all threads) that are waiting on the condition variable. These methods do not release the lock.
    public void put(T element) {
        lock.lock();
        try {
            queue.add(element);
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public T get() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                notEmpty.await();
            }
            return queue.poll();
        } finally {
            lock.unlock();
        }
    }
}

public class MultiProducerMultiConsumerDemo {
    public static void main(String[] args) throws InterruptedException {
        Channel<Integer> channel = new Channel<>();

        Thread producer1 = new Thread(() -> {
            try {
                for (int i = 1; i <= 42; i++) {
                    channel.put(i);
                    System.out.println("producer-1: " + i);
                }
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread producer2 = new Thread(() -> {
            try {
                for (int i = 1; i <= 42; i++) {
                    channel.put(i + 42);
                    System.out.println("producer-2: " + i);
                }
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer1 = new Thread(() -> {
            try {
                for (int i = 1; i <= 42; i++) {
                    Integer element = channel.get();
                    System.out.println("consumer-1: " + element);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer2 = new Thread(() -> {
            try {
                for (int i = 1; i <= 42; i++) {
                    Integer element = channel.get();
                    System.out.println("consumer-2: " + element);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer1.start();
        producer2.start();
        consumer1.start();
        consumer2.start();

        producer1.join();
        producer2.join();
        consumer1.join();
        consumer2.join();

        // consumer1.interrupt();
        // consumer2.interrupt();

        System.out.println("main: done");
    }
}
