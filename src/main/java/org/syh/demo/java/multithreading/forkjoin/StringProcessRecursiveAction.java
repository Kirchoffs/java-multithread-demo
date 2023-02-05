package org.syh.demo.java.multithreading.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class StringProcessRecursiveAction extends RecursiveAction {
    private String workload = "";
    private static final int THRESHOLD = 5;

    public StringProcessRecursiveAction(String workload) {
        this.workload = workload;
    }

    @Override
    protected void compute() {
        if (workload.length() > THRESHOLD) {
            ForkJoinTask.invokeAll(createSubtasks());
        } else {
            processing(workload);
        }
    }

    private List<StringProcessRecursiveAction> createSubtasks() {
        List<StringProcessRecursiveAction> subtasks = new ArrayList<>();

        String partOne =
                workload.substring(0, workload.length() / 2);
        String partTwo =
                workload.substring(workload.length() / 2);

        subtasks.add(new StringProcessRecursiveAction(partOne));
        subtasks.add(new StringProcessRecursiveAction(partTwo));

        return subtasks;
    }

    private void processing(String work) {
        String result = work.toUpperCase();
        System.out.println(
            String.format("This subtask %s was processed by %s", result, Thread.currentThread().getName())
        );
    }

    public static void main(String[] args) {
        String workload =
                "Oak Maple Pine Birch Cherry Apple Elm Magnolia Dogwood Willow Redwood " +
                "Cedar Spruce Holly Palm Ginkgo Sequoia Douglas Fir Aspen Cypress";

        StringProcessRecursiveAction task = new StringProcessRecursiveAction(workload);
        task.fork();
        task.join();
        System.out.println("Done");
    }
}
