package org.syh.demo.java.multithreading.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureExceptionFirstDemo {
    public static void main(String[] args) {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1822);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            throw new CustomizedException("Who can catch me?");
        });

        try {
            future.get();
        } catch (InterruptedException e) {
            System.out.println("Caught InterruptedException");
        } catch (ExecutionException e) {
            System.out.println("Caught ExecutionException");
            System.out.println(e.getCause().getMessage());
        }
    }

    private static class CustomizedException extends RuntimeException {
        public CustomizedException(String message) {
            super(message);
        }
    }
}
