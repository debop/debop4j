package kr.debop4j.core.guava.eventbus.subscriber;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;

import java.util.List;

/**
 * kr.nsoft.commons.guava.eventbus.subscriber.EventSubscriber
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 10.
 */
public abstract class EventSubscriber<T> {

    List<T> events = Lists.newArrayList();

    public EventSubscriber(EventBus eventBus) {
        eventBus.register(this);
    }

    public abstract void handleEvent(T event);

    public List<T> getHandledEvents() {
        return events;
    }
}
