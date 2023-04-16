package org.syh.demo.java.multithreading.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CompletableFutureSixthDemo {
    public static void main(String[] args) throws Exception {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
               throw new IllegalStateException(e);
            }
            return "agetus";
        }).thenAccept(agetus -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
               throw new IllegalStateException(e);
            }

            System.out.println("bellus " + agetus);
        }).thenRun(() -> {
            System.out.println("callistus");
        });
        
        System.out.println(future.get());
    }
}
