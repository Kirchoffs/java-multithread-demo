package org.syh.demo.java.multithreading.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CompletableFutureCompositionDemo {
    public static void main(String[] args) {
        anyOfDemo();
        allOfDemo();
    }

    private static void anyOfDemo() {
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Hello, World from f1!";
        });

        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Hello, World from f2!";
        });

        CompletableFuture<Object> f = CompletableFuture.anyOf(f1, f2);
        System.out.println(f.join());
    }

    private static void allOfDemo() {
        CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 42;
        });

        CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 89;
        });

        CompletableFuture<Integer> f = CompletableFuture.allOf(f1, f2).thenApply(
            param -> f1.join() + f2.join()
        );
        System.out.println(f.join());
    }
}
