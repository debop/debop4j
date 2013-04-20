package kr.debop4j.core.guava.eventbus.subscriber;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import kr.debop4j.core.guava.eventbus.events.CashPurchaseEvent;
import kr.debop4j.core.guava.eventbus.events.CreditPurchaseEvent;

import java.util.concurrent.CountDownLatch;

/**
 * kr.debop4j.core.guava.eventbus.subscriber.LongProcessSubscriber
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 12. 10.
 */
public class LongProcessSubscriber {

    private CountDownLatch doneSignal;

    public LongProcessSubscriber(AsyncEventBus asyncEventBus, CountDownLatch doneSignal) {
        asyncEventBus.register(this);
        this.doneSignal = doneSignal;
    }

    @Subscribe
    @AllowConcurrentEvents
    public void handleEventConcurrent(CreditPurchaseEvent event) {
        pause(300L);
        doneSignal.countDown();
    }

    @Subscribe
    public void handleEventConcurrent(CashPurchaseEvent event) {
        pause(300L);
        doneSignal.countDown();
    }

    private void pause(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ignored) {
        }
    }
}
