package kr.debop4j.core.guava.concurrency;

import com.google.common.util.concurrent.Monitor;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * kr.debop4j.core.guava.concurrency.MonitorExample
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 12. 10.
 */
public class MonitorExample {

    private final Monitor monitor = new Monitor();
    private volatile boolean condition = true;
    private int taskDoneCounter;
    private AtomicInteger taskSkippedCounter = new AtomicInteger(0);
    private int stopTaskCount;

    private Monitor.Guard conditionGuard = new Monitor.Guard(monitor) {
        @Override
        public boolean isSatisfied() {
            return condition;
        }
    };

    public void demoTryEnterIf() throws InterruptedException {
        if (monitor.tryEnterIf(conditionGuard)) {
            try {
                simulatedWork();
                taskDoneCounter++;
            } finally {
                monitor.leave();
            }
        } else {
            taskSkippedCounter.incrementAndGet();
        }
    }


    private void simulatedWork() throws InterruptedException {
        Thread.sleep(250);
    }

    public void reEvaluateGuardCondition() {
        try {
            monitor.enterWhen(conditionGuard);
        } catch (InterruptedException ignored) {
        }
    }

    public int getStopTaskCount() {
        return stopTaskCount;
    }

    public void setStopTaskCount(int stopTaskCount) {
        this.stopTaskCount = stopTaskCount;
    }

    public void setCondition(boolean condition) {
        this.condition = condition;
    }

    public int getTaskSkippedCounter() {
        return taskSkippedCounter.get();
    }

    public int getTaskDoneCounter() {
        return taskDoneCounter;
    }
}
