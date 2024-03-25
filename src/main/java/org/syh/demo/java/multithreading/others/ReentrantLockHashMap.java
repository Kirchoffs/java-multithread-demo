package org.syh.demo.java.multithreading.others;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockHashMap {
    private Map<Integer, Integer> map;
    private ReentrantLock lock;

    public ReentrantLockHashMap() {
        map = new HashMap<>();
        lock = new ReentrantLock();
    }

    public void put(int key, int value) {
        lock.lock();
        map.put(key, value);
        lock.unlock();
    }

    public int get(int key) {
        lock.lock();
        int value = map.getOrDefault(key, -1);
        lock.unlock();
        return value;
    }

    public static void main(String[] args) {
        ReentrantLockHashMap map = new ReentrantLockHashMap();

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
