package kr.debop4j.core.guava.eventbus.subscriber;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

/**
 * kr.nsoft.commons.guava.eventbus.subscriber.InvalidSubscriberNoParameters
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 10.
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
