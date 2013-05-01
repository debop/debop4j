package kr.debop4j.core.guava.eventbus.subscriber;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import kr.debop4j.core.guava.eventbus.events.CashPurchaseEvent;

/**
 * kr.debop4j.core.guava.eventbus.subscriber.CashPurchaseEventSubscriber
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 10.
 */
public class CashPurchaseEventSubscriber extends EventSubscriber<CashPurchaseEvent> {

    public CashPurchaseEventSubscriber(EventBus eventBus) {
        super(eventBus);
    }

    @Override
    @Subscribe
    public void handleEvent(CashPurchaseEvent event) {
        events.add(event);
    }
}
