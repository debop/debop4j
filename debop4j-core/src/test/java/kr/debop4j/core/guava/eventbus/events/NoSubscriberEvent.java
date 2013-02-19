package kr.debop4j.core.guava.eventbus.events;

import lombok.Getter;

/**
 * kr.debop4j.core.guava.eventbus.events.NoSubscriberEvent
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 10.
 */
public class NoSubscriberEvent {

    @Getter
    String message = "I'm lost";
}
