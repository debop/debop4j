package kr.debop4j.core.guava.eventbus.events;

import lombok.Getter;

/**
 * kr.debop4j.core.guava.eventbus.events.SimpleEvent
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 12. 10.
 */
public class SimpleEvent {

    @Getter
    private String name;

    public SimpleEvent(String name) {
        this.name = name;
    }
}
