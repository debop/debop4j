package kr.debop4j.core.guava.eventbus.subscriber;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

/**
 * kr.debop4j.core.guava.eventbus.subscriber.AllEventSubscriber
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 12. 10.
 */
public class AllEventSubscriber extends EventSubscriber<Object> {

    public AllEventSubscriber(EventBus eventBus) {
        super(eventBus);
    }

    @Override
    @Subscribe
    public void handleEvent(Object event) {
        events.add(this);
    }
}
