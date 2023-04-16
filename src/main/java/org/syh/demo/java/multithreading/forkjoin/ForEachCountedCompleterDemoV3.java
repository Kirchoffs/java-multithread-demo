package org.syh.demo.java.multithreading.forkjoin;

import java.util.concurrent.CountedCompleter;

public class ForEachCountedCompleterDemoV3 {
    public static void main(String[] args) {
        String[] arr = new String[100];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i + "";
        }
        ForEach.forEach(arr, (str) -> System.out.println(str + str));
    }
}

interface CustomizedOperation<E> {
    void apply(E e);
}

class ForEach<E> extends CountedCompleter<Void> {
    public static <E> void forEach(E[] array, CustomizedOperation<E> op) {
        new ForEach<E>(null, array, op, 0, array.length).invoke();
    }
 
    final E[] array; 
    final CustomizedOperation<E> op; 
    final int lo, hi;

    private ForEach(CountedCompleter<?> p, E[] array, CustomizedOperation<E> op, int lo, int hi) {
        super(p);
        this.array = array; 
        this.op = op; 
        this.lo = lo; 
        this.hi = hi;
    }
 
    public void compute() {
        int l = lo, h = hi;
        
        while (h - l >= 2) {
            int m = l + (h - l) / 2;
            setPendingCount(2);
            new ForEach(this, array, op, m, h).fork();
            h = m;
        }

        if (h > l) {
            op.apply(array[l]);
        }
        propagateCompletion();
    }
}
 