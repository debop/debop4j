package kr.debop4j.core.guava.eventbus.events;

import lombok.Getter;

/**
 * kr.debop4j.core.guava.eventbus.events.NoSubscriberEvent
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 10.
 */
public class NoSubscriberEvent {

    @Getter
    String message = "I'm lost";
}
