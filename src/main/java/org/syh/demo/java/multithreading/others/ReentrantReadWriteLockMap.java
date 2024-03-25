package org.syh.demo.java.multithreading.others;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockMap {
    private Map<Integer, Integer> map;
    private ReadWriteLock lock;
    private Lock writeLock;
    private Lock readLock;

    public ReentrantReadWriteLockMap() {
        map = new HashMap<>();
        lock = new ReentrantReadWriteLock();
        writeLock = lock.writeLock();
        readLock = lock.readLock();
    }

    public void put(int key, int value) {
        writeLock.lock();
        map.put(key, value);
        writeLock.unlock();
    }

    public int get(int key) {
        readLock.lock();
        int value = map.getOrDefault(key, -1);
        readLock.unlock();
        return value;
    }

    public static void main(String[] args) {
        ReentrantReadWriteLockMap map = new ReentrantReadWriteLockMap();

        int numWrite = 5;
        int numRead = 2;
        
        for (int i = 0; i < numWrite; i++) {
            int j = i;
            new Thread(() -> {
                map.put(j, j);
            }).start();
        }

        for (int i = 0; i < numRead; i++) {
            int j = i;
            new Thread(() -> {
                System.out.println(map.get(j));
            }).start();
        }
    }
}
