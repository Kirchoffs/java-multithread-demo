package org.syh.demo.java.multithreading.others;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// A batch enqueuing thread could be executed interleaved with another batch enqueuing thread
public class BlockingQueueV1<T> {
    private final Queue<T> queue = new ArrayDeque<>();
    private final int capacity;

    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();
    private boolean isBatchEnqueueing = false;

    public BlockingQueueV1(int capacity) {
        this.capacity = capacity;
    }

    public void enqueue(T item) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == capacity || isBatchEnqueueing) {
                notFull.await();
            }
            queue.offer(item);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public void batchEnqueue(List<T> items) throws InterruptedException {
        lock.lock();
        try {
            isBatchEnqueueing = true;
            int itemsAdded = 0;
            while (itemsAdded < items.size()) {
                while (queue.size() == capacity) {
                    notFull.await();
                }
                int spaceAvailable = capacity - queue.size();
                int itemsToAdd = Math.min(spaceAvailable, items.size() - itemsAdded);

                for (int i = 0; i < itemsToAdd; i++) {
                    queue.offer(items.get(itemsAdded + i));
                }
                itemsAdded += itemsToAdd;

                notEmpty.signalAll();
            }
        } finally {
            isBatchEnqueueing = false;
            notFull.signalAll();
            lock.unlock();
        }
    }

    public T dequeue() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                notEmpty.await();
            }
            T item = queue.poll();
            notFull.signalAll();
            return item;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        BlockingQueueV1<String> queue = new BlockingQueueV1<>(8);

        List<Thread> singleProducers = new LinkedList<>();
        for (int i = 1; i <= 6; i++) {
            final int id = i;
            Thread producer = new Thread(() -> {
                try {
                    for (int j = 1; j <= 7; j++) {
                        queue.enqueue("single-producer-" + id + "-" + j);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            singleProducers.add(producer);
            producer.start();
        }

        Thread alpahBatchProducer = new Thread(() -> {
            try {
                for (int i = 1; i <= 3; i++) {
                    List<String> items = new LinkedList<>();
                    for (int j = 1; j <= 7; j++) {
                        items.add("alpha-batch-producer-" + i + "-" + j);
                    }
                    queue.batchEnqueue(items);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        alpahBatchProducer.start();

        Thread betaBatchProducer = new Thread(() -> {
            try {
                for (int i = 1; i <= 3; i++) {
                    List<String> items = new LinkedList<>();
                    for (int j = 1; j <= 7; j++) {
                        items.add("beta-batch-producer-" + i + "-" + j);
                    }
                    queue.batchEnqueue(items);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        betaBatchProducer.start();

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 84; i++) {
                    System.out.println(queue.dequeue());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        consumer.start();

        try {
            for (Thread singleProducer : singleProducers) {
                singleProducer.join();
            }
            alpahBatchProducer.join();
            betaBatchProducer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
