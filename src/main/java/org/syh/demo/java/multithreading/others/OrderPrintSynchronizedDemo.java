package org.syh.demo.java.multithreading.others;

public class OrderPrintSynchronizedDemo {
    public static void main(String[] args) {
        OrderPrintService orderPrintService = new OrderPrintService();

        new Thread(() -> {
            try {
                orderPrintService.third();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                orderPrintService.second();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                orderPrintService.first();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}

class OrderPrintService {
    private boolean second;
    private boolean third;

    public OrderPrintService() {
        second = false;
        third = false;
    }

    public void first() throws InterruptedException {
        synchronized(this) {
            System.out.println("first");
            second = true;
            this.notifyAll();
        }
    }

    public void second() throws InterruptedException {
        synchronized(this) {
            while (!second) {
                this.wait();
            }
            System.out.println("second");
            third = true;
            this.notifyAll();
        }
    }

    public void third() throws InterruptedException {
        synchronized(this) {
            while (!third) {
                this.wait();
            }
            System.out.println("third");
        }
    }
}
