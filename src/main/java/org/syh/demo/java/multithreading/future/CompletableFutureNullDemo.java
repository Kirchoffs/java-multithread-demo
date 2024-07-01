package org.syh.demo.java.multithreading.future;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureNullDemo {
    public static void main(String[] args) throws Exception {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("Processing null...");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        });

        System.out.println(future.get());
    }
}
