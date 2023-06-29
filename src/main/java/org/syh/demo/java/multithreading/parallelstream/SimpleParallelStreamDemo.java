package org.syh.demo.java.multithreading.parallelstream;

import java.util.Arrays;
import java.util.stream.IntStream;

public class SimpleParallelStreamDemo {
    public static void main(String[] args) {
        int[] a = {1, 2, 3};
        int[] b = {4, 5, 6};
        int[] c = new int[3];

        IntStream.range(0, c.length).parallel().forEach(i -> c[i] = a[i] + b[i]);
        System.out.println(Arrays.toString(c));

        int[] d = IntStream.range(0, c.length).parallel().map(i -> a[i] + b[i]).toArray();
        System.out.println(Arrays.toString(d));
    }
}
