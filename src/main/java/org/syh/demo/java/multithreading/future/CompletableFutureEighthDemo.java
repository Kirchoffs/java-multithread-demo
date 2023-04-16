package org.syh.demo.java.multithreading.future;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureEighthDemo {
    public static void main(String[] args) throws Exception {
        CompletableFuture<String> cf = new CompletableFuture<>();

        new Thread(() -> {
            try {
                Thread.sleep(2000);
                if (Math.random() > 0.5) {
                    cf.complete("Hello");
                } else {
                    cf.cancel(true);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        System.out.println(cf.get()); // It will block until the future is completed or cancelled after 2 seconds.
    }
}
