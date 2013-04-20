package kr.debop4j.core.guava.eventbus;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Guava 의 EventBus 를 이용하여 Publisher - Subscriber 패턴을 구현합니다.
 * 인터페이스가 필요 없고, Annotation만을 사용하므로 상당히 자유롭고 편합니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 12. 11
 */
@Slf4j
public class EventBusListenerTest {

    @Test
    public void shouldReceiveEvent() throws Exception {
        EventBus eventBus = new EventBus("test");
        EventListener listener = new EventListener();

        // register listener / subscriber
        eventBus.register(listener);

        // publish event
        eventBus.post(new OurTestEvent(200));

        assertThat(listener.getLastMessage(), is(200));
    }

    @Test
    public void shouldReceiveMultipleEvents() throws Exception {
        EventBus eventBus = new EventBus("multiple");
        MultipleListener listener = new MultipleListener();

        eventBus.register(listener);
        eventBus.post(100);
        eventBus.post(800L);

        assertEquals(100, listener.getLastInteger().intValue());
        assertEquals(800L, listener.getLastLong().longValue());
    }

    @Test
    public void shouldDetectEventWithoutListeners() throws Exception {
        EventBus eventBus = new EventBus("dead");

        DeadEventListner deadEventListner = new DeadEventListner();
        eventBus.register(deadEventListner);

        eventBus.post(new OurTestEvent(200));

        assertTrue(deadEventListner.isNotDelivered());
    }


    class EventListener {

        @Getter
        private int lastMessage = 0;

        @Subscribe
        public void listen(OurTestEvent event) {
            lastMessage = event.getMessage();
        }
    }

    class OurTestEvent {

        @Getter
        private final int message;

        public OurTestEvent(int message) {
            this.message = message;
        }
    }


    class MultipleListener {

        @Getter
        private Integer lastInteger;
        @Getter
        private Long lastLong;

        @Subscribe
        public void listenInteger(Integer event) {
            lastInteger = event;
        }

        @Subscribe
        public void listenLong(Long event) {
            lastLong = event;
        }
    }

    class DeadEventListner {

        @Getter
        boolean notDelivered = false;

        // DeadEvent 는 모든 Event 발행 시 Subscriber가 받을 수 있다. Publish 한 내용보다, Publish 했다는 사실이 중요할 때 사용한다.
        @Subscribe
        public void listen(DeadEvent event) {
            notDelivered = true;
        }
    }
}


