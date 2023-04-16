package org.syh.demo.java.multithreading.future;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class CompletableFutureSeventhDemo {
    public static void main(String[] args) throws Exception {
        Calculator calc = new Calculator(4, 7);
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(calc);
        future.thenAccept(result -> {
            System.out.println(result);
        });
        System.out.println("CompletableFutureTest starts.... ");
        Thread.sleep(10000);
        System.out.println("CompletableFutureTest ends.... ");
    }
}

class Calculator implements Supplier<Integer> {
    private int x, y;

    public Calculator(int x, int y) {
       this.x = x;
       this.y = y;
    }

    @Override
    public Integer get() {
       try {
          Thread.sleep(3000);
       } catch(InterruptedException e) {
          e.printStackTrace();
       }
       return x + y;
    }
 }
