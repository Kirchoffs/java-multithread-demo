package org.syh.demo.java.multithreading.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureExceptionSecondDemo {
    public static void main(String[] args) {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1822);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return "Caught InterruptedException";
            }
            throw new CustomizedException("Who can catch me?");
        }).exceptionally(e -> {
            System.out.println("Caught exception: " + e.getMessage());
            return "Caught exception";
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
