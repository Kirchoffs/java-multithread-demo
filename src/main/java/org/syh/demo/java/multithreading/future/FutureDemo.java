package org.syh.demo.java.multithreading.future;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureDemo {
    public static void main(String[] args) throws Exception {
        Future<Integer> future = new SquareCalculator().calculate(10);
        while (!future.isDone()) {
            System.out.println("Calculating...");
            Thread.sleep(300);
        }
        Integer result = future.get();
        System.out.println(result);
    }
}

class SquareCalculator {    
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    
    public Future<Integer> calculate(Integer input) {        
        return executor.submit(() -> {
            Thread.sleep(1000);
            return input * input;
        });
    }
}