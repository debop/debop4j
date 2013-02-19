package kr.debop4j.core.guava.eventbus.subscriber;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import kr.debop4j.core.guava.eventbus.events.CreditPurchaseEvent;

/**
 * kr.debop4j.core.guava.eventbus.subscriber.InvalidSubscriberMultipleParameter
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 10.
 */
public class InvalidSubscriberMultipleParameter {

    public InvalidSubscriberMultipleParameter(EventBus eventBus) {
        eventBus.register(this);
    }

    @Subscribe
    public void handleCreditEvent(CreditPurchaseEvent event, Object foo) {
        // Do nothing this will not work
    }
}
