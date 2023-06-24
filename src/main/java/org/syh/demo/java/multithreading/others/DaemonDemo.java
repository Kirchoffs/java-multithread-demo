package org.syh.demo.java.multithreading.others;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class DaemonDemo {
    public static void main(String[] args) {
        Sweeper sweeper = new Sweeper();
        sweeper.start();

        Worker workerAlpha = new Worker(new Date());
        Worker workerBeta = new Worker(new Date());
        Worker workerGamma = new Worker(new Date());

        sweeper.addWorker(workerAlpha);
        sweeper.addWorker(workerBeta);
        sweeper.addWorker(workerGamma);

        workerAlpha.start();
        workerBeta.start();
        workerGamma.start();
    }
}

class Worker extends Thread {
    private Date beginTime;
    private Date endTime;

    private boolean isDone;

    public Worker(Date beginTime) {
        this.beginTime = beginTime;
    }

    public void run() {
        Thread self = Thread.currentThread();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        String beginTimeStr = sdf.format(this.beginTime);
        System.out.println(self.getName() + " begins at " + beginTimeStr);

        Calendar cal = Calendar.getInstance();
        cal.setTime(beginTime);

        Random random = new Random();
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (Exception e) {}

            if (isDone) {
                String endTimeStr = sdf.format(this.endTime);
                System.out.println(self.getName() + " ends at " + endTimeStr);
                break;
            } else {
                int hour = random.nextInt(5);
                cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + hour);
                endTime = cal.getTime();
            }
        }
    }

    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }   

    public boolean isDone() {
        return this.isDone;
    }   

    public Date getEndTime() {
        return this.endTime;
    }   

    public double getWorkingTime() {
        return (endTime.getTime() - beginTime.getTime()) / 1000 / 60 / 60;
    }
}

class Sweeper extends Thread {
    private List<Worker> workers = new ArrayList<>();

    public Sweeper() {
        this.setDaemon(true);
    } 

    public void run() {
        while (true) {
            for (int i = 0; i < workers.size(); i++) {
                Worker worker = workers.get(i);
                if (worker.getEndTime() != null && !worker.isDone()) {
                    double workingTime = worker.getWorkingTime();
                    if (workingTime >= 8) {
                        worker.setDone(true);
                        System.out.println(worker.getName() + " has worked for " + workingTime + " hours, and will get off work.");
                    } else {
                        System.out.println(worker.getName() + " has worked for " + workingTime + " hours, and will continue to work.");
                    }
                }
                
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (Exception e) {}
            }
        }
    }

    public void addWorker(Worker worker) {
        this.workers.add(worker);
    }
}
