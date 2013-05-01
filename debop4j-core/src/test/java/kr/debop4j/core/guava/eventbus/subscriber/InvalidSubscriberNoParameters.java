package kr.debop4j.core.guava.eventbus.subscriber;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

/**
 * kr.debop4j.core.guava.eventbus.subscriber.InvalidSubscriberNoParameters
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 10.
 */
public class InvalidSubscriberNoParameters {

    public InvalidSubscriberNoParameters(EventBus eventBus) {
        eventBus.register(this);
    }

    @Subscribe
    public void handleEvent() {
        // Do nothing will not work
    }
}
