package org.syh.demo.java.multithreading.monitor;

public class SynchronizedConditionDemo {
    private static class SimpleTask {
        private boolean flag = false;
    
        public synchronized void waitForSignal() {
            while (!flag) {
                try {
                    System.out.println(Thread.currentThread().getName() + " is waiting...");
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Thread interrupted while waiting.");
                }
            }
            System.out.println(Thread.currentThread().getName() + " received the signal!");
        }
    
        public synchronized void sendSignal() {
            flag = true;
            System.out.println(Thread.currentThread().getName() + " sent the signal!");
            notify();
        }
    }

    public static void main(String[] args) {
        SimpleTask task = new SimpleTask();

        Thread t1 = new Thread(() -> {
            task.waitForSignal();
        }, "Thread-1");

        Thread t2 = new Thread(() -> {
            task.sendSignal();
        }, "Thread-2");

        t1.start();
        t2.start();
    }
}
