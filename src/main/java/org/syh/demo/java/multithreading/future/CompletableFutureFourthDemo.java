package org.syh.demo.java.multithreading.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CompletableFutureFourthDemo {
    public static void main(String[] args) throws Exception {
        CompletableFuture<String> worldFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return "World";
         });
         
         CompletableFuture<String> helloFuture = worldFuture.thenApply(world -> {
            return "Hello " + world;
         });
         
         System.out.println(helloFuture.get());
    }
}
