package org.syh.demo.java.multithreading.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CompletableFutureFirstDemo {
    public static void main(String[] args) throws Exception {
        Future<String> completableFuture = calculateAsync();
        String result = completableFuture.get();
        System.out.println(result);
    }

    public static Future<String> calculateAsync() throws InterruptedException {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
    
        Executors.newCachedThreadPool().submit(() -> {
            Thread.sleep(500);
            completableFuture.complete("Hello");
            return null;
        });
    
        return completableFuture;
    }
}
