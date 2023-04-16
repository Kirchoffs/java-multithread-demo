package org.syh.demo.java.multithreading.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CompletableFutureFifthDemo {
    public static void main(String[] args) throws Exception {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
               throw new IllegalStateException(e);
            }
            return "agetus";
        }).thenApply(agetus -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
               throw new IllegalStateException(e);
            }

            return "bellus " + agetus;
        }).thenApply(bellus -> {
            return "callistus " + bellus;
        });
        
        System.out.println(future.get());
    }
}
