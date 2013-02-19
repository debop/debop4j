package kr.debop4j.core.guava.eventbus.subscriber;

import com.google.common.eventbus.EventBus;
import kr.debop4j.core.guava.eventbus.events.PurchaseEvent;

/**
 * kr.nsoft.commons.guava.eventbus.subscriber.PurchaseEventSubscriber
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 10.
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
