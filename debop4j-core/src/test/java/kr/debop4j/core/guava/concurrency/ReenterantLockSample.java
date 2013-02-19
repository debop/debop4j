package kr.debop4j.core.guava.concurrency;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * kr.nsoft.commons.guava.concurrency.ReenterantLockSample
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 10.
 */
public class ReenterantLockSample {

    private List<String> list = Lists.newArrayList();
    private static final int MAX_SIZE = 10;


    private ReentrantLock rLock = new ReentrantLock();
    private Condition listAtCapacity = rLock.newCondition();

    public void addList(String item) throws InterruptedException {
        rLock.lock();
        try {
            while (list.size() == MAX_SIZE) {
                listAtCapacity.await();
            }
            list.add(item);
        } finally {
            rLock.unlock();
        }
    }
}
