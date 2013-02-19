package kr.debop4j.core.guava.eventbus.events;

import lombok.Getter;

/**
 * kr.nsoft.commons.guava.eventbus.events.SimpleEvent
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 10.
 */
public class SimpleEvent {

    @Getter
    private String name;

    public SimpleEvent(String name) {
        this.name = name;
    }
}
