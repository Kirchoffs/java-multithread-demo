package org.syh.demo.java.multithreading.others;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// A batch enqueuing thread should not be executed interleaved with another batch enqueuing thread or a single enqueuing thread
public class BlockingQueueV2<T> {
    private final Queue<T> queue = new ArrayDeque<>();

    private final int capacity;

    private final Lock lock = new ReentrantLock();
    private Thread batchEnqueuingThread = null;
    private final Condition enqueueFlag = lock.newCondition();
    private final Condition dequeueFlag = lock.newCondition();

    public BlockingQueueV2(int capacity) {
        this.capacity = capacity;
    }

    public void enqueue(T item) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == capacity || batchEnqueuingThread != null) {
                enqueueFlag.await();
            }

            queue.offer(item);
            dequeueFlag.signal();
        } finally {
            lock.unlock();
        }
    }

    public void batchEnqueue(List<T> items) throws InterruptedException {
        lock.lock();
        try {
            int itemsAdded = 0;
            while (itemsAdded < items.size()) {
                while (queue.size() == capacity || (batchEnqueuingThread != null && batchEnqueuingThread != Thread.currentThread())) {
                    enqueueFlag.await();
                }
                batchEnqueuingThread = Thread.currentThread();
                int spaceAvailable = capacity - queue.size();
                int itemsToAdd = Math.min(spaceAvailable, items.size() - itemsAdded);

                for (int i = 0; i < itemsToAdd; i++) {
                    queue.offer(items.get(itemsAdded + i));
                }
                itemsAdded += itemsToAdd;

                dequeueFlag.signalAll();
            }
        } finally {
            batchEnqueuingThread = null;
            enqueueFlag.signalAll();
            lock.unlock();
        }
    }

    public T dequeue() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                dequeueFlag.await();
            }
            T item = queue.poll();
            enqueueFlag.signalAll();
            return item;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        BlockingQueueV2<String> queue = new BlockingQueueV2<>(8);

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
