package kr.debop4j.core.guava.concurrency;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Monitor;

import java.util.List;

/**
 * kr.debop4j.core.guava.concurrency.MonitorSample
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 12. 10.
 */
public class MonitorSample {

    private List<String> list = Lists.newArrayList();
    private static final int MAX_SIZE = 10;

    private Monitor monitor = new Monitor();

    private Monitor.Guard listBelowCapacity =
            new Monitor.Guard(monitor) {
                @Override
                public boolean isSatisfied() {
                    return (list.size() < MAX_SIZE);
                }
            };

    private void addToList(String item) throws InterruptedException {
        monitor.enterWhen(listBelowCapacity);
        try {
            list.add(item);
        } finally {
            monitor.leave();
        }
    }
}
