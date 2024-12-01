package org.syh.demo.java.multithreading.monitor;

public class SynchronizedConditionWithTimeDemo {
    private static class SimpleTask {
        private boolean flag = false;
    
        public synchronized void waitForSignal() {
            while (!flag) {
                try {
                    System.out.println(Thread.currentThread().getName() + " is waiting...");
                    wait(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Thread interrupted while waiting.");
                }
                System.out.println(Thread.currentThread().getName() + " is going to check the signal again.");
            }
            System.out.println(Thread.currentThread().getName() + " received the signal!");
        }
    
        public synchronized void sendSingleSignal() {
            flag = true;
            System.out.println(Thread.currentThread().getName() + " sent the signal!");
            notify();
        }

        public void sendMultipleSignals() {
            Boolean[] flags = {false, false, false, false, true};
            for (int i = 0; i < flags.length; i++) {
                synchronized (this) {
                    flag = flags[i];
                    System.out.println(Thread.currentThread().getName() + " sent the signal!");

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("Thread interrupted while sending signal.");
                    }

                    System.out.println(Thread.currentThread().getName() + " is going to notify the waiting thread.");
                    notify();
                }
            }
        }
    }

    private static void singleSignalWithDelay() throws InterruptedException {
        SimpleTask task = new SimpleTask();

        Thread t1 = new Thread(() -> {
            task.waitForSignal();
        }, "Thread-1");

        Thread t2 = new Thread(() -> {
            task.sendSingleSignal();
        }, "Thread-2");

        t1.start();
        Thread.sleep(5000);
        t2.start();
    }

    private static void multipleSignalsWithDelay() throws InterruptedException {
        SimpleTask task = new SimpleTask();

        Thread t1 = new Thread(() -> {
            task.waitForSignal();
        }, "Thread-1");

        Thread t2 = new Thread(() -> {
            task.sendMultipleSignals();
        }, "Thread-2");

        t1.start();
        t2.start();
    }

    public static void main(String[] args) throws InterruptedException {
        singleSignalWithDelay();
        Thread.sleep(1000);
        System.out.println("--------------------");
        multipleSignalsWithDelay();
    }
}
