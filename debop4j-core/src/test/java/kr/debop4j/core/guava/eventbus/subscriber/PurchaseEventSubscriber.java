package kr.debop4j.core.guava.eventbus.subscriber;

import com.google.common.eventbus.EventBus;
import kr.debop4j.core.guava.eventbus.events.PurchaseEvent;

/**
 * kr.debop4j.core.guava.eventbus.subscriber.PurchaseEventSubscriber
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 10.
 */
public class PurchaseEventSubscriber extends EventSubscriber<PurchaseEvent> {

    public PurchaseEventSubscriber(EventBus eventBus) {
        super(eventBus);
    }

    @Override
    public void handleEvent(PurchaseEvent event) {
        events.add(event);
    }
}
