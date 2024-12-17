package org.syh.demo.java.multithreading.future;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class FutureTaskDemo {
    public static void main(String[] args) throws Exception {
        FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                TimeUnit.SECONDS.sleep(2);
                return "Hello, World!";
            }
        });

        new Thread(futureTask).start();
        System.out.println(futureTask.get());
        System.out.println("Done");
    }
}
