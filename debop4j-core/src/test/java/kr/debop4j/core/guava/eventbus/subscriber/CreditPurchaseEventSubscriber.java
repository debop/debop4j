package kr.debop4j.core.guava.eventbus.subscriber;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import kr.debop4j.core.guava.eventbus.events.CreditPurchaseEvent;

/**
 * kr.debop4j.core.guava.eventbus.subscriber.CreditPurchaseEventSubscriber
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 10.
 */
public class CreditPurchaseEventSubscriber extends EventSubscriber<CreditPurchaseEvent> {

    public CreditPurchaseEventSubscriber(EventBus eventBus) {
        super(eventBus);
    }

    @Override
    @Subscribe
    public void handleEvent(CreditPurchaseEvent event) {
        events.add(event);
    }
}
