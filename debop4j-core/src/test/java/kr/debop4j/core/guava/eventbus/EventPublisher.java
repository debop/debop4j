package kr.debop4j.core.guava.eventbus;

import com.google.common.eventbus.EventBus;
import kr.debop4j.core.guava.eventbus.events.CashPurchaseEvent;
import kr.debop4j.core.guava.eventbus.events.CreditPurchaseEvent;
import kr.debop4j.core.guava.eventbus.events.NoSubscriberEvent;
import kr.debop4j.core.guava.eventbus.events.SimpleEvent;

/**
 * kr.debop4j.core.guava.eventbus.EventPublisher
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 10.
 */
// 참고 : https://github.com/bbejeck/guava-blog/tree/master/src/main/java/bbejeck/guava/eventbus
public class EventPublisher {

    EventBus eventBus;

    public EventPublisher(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void createCashPurchaseEvent(String description, long amount) {
        eventBus.post(new CashPurchaseEvent(amount, description));
    }

    public void createCreditPurchaseEvent(String item, String ccNumber, long amount) {
        eventBus.post(new CreditPurchaseEvent(amount, ccNumber, item));
    }

    public void createSimpleEvent(String eventName) {
        eventBus.post(new SimpleEvent(eventName));
    }

    public void createNoSubscribedEvent() {
        eventBus.post(new NoSubscriberEvent());
    }
}
