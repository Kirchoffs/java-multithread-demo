package org.syh.demo.java.multithreading.others;

import java.util.concurrent.TimeUnit;

public class PriorityDemo {
    public static void main(String[] args) {
        TicketTask task = new TicketTask();
        
        Thread t1 = new Thread(task, "Level Alpha");
        t1.setPriority(Thread.MAX_PRIORITY);

        Thread t2 = new Thread(task, "Level Beta");

        Thread t3 = new Thread(task, "Level Gamma");
        t3.setPriority(Thread.MIN_PRIORITY);

        t1.start();
        t2.start();
        t3.start();

        System.out.println(t1.getName());
        System.out.println(t2.getName());
        System.out.println(t3.getName());
    }
}

class TicketTask implements Runnable {
    private Integer ticket = 30;

    public void run() {
        while (this.ticket > 0) {
            synchronized (this) {
                if (this.ticket > 0) {
                    System.out.println("Window " + Thread.currentThread().getName() + " sells ticket one ticket.");
                    ticket--;
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (Exception e) {}
                }
            }
        }
    }
}
