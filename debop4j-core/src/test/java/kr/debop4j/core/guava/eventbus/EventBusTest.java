package kr.debop4j.core.guava.eventbus;

import com.google.common.collect.Lists;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import kr.debop4j.core.guava.eventbus.events.CashPurchaseEvent;
import kr.debop4j.core.guava.eventbus.subscriber.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;

/**
 * kr.debop4j.core.guava.eventbus.EventBusTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 10.
 */
@Slf4j
public class EventBusTest {

    private EventPublisher eventPublisher;
    private CashPurchaseEventSubscriber cashPurchaseEventSubscriber;
    private CreditPurchaseEventSubscriber creditPurchaseEventSubscriber;
    private PurchaseEventSubscriber purchaseEventSubscriber;
    private EventBus eventBus;
    private AsyncEventBus asyncEventBus;
    private LongProcessSubscriber longProcessSubscriber;
    private DeadEventSubscriber deadEventSubscriber;
    private AllEventSubscriber allEventSubscriber;
    private MultiHandlerSubscriber multiHandlerSubscriber;
    private CountDownLatch doneSignal;
    private int numberLongEvents = 10;

    @Before
    public void setUp() {
        eventBus = new EventBus();
        deadEventSubscriber = new DeadEventSubscriber();
        eventBus.register(deadEventSubscriber);
        eventPublisher = new EventPublisher(eventBus);

        cashPurchaseEventSubscriber = new CashPurchaseEventSubscriber(eventBus);
        creditPurchaseEventSubscriber = new CreditPurchaseEventSubscriber(eventBus);
        purchaseEventSubscriber = new PurchaseEventSubscriber(eventBus);
        multiHandlerSubscriber = new MultiHandlerSubscriber(eventBus);
    }

    @Test
    public void cashPurchaseEventReceived() {
        generateCashPurchaseEvent();
        assertEquals(1, cashPurchaseEventSubscriber.getHandledEvents().size());
        assertEquals(0, creditPurchaseEventSubscriber.getHandledEvents().size());
        assertEquals(CashPurchaseEvent.class, cashPurchaseEventSubscriber.getHandledEvents().get(0).getClass());
        assertEquals(0, deadEventSubscriber.deadEvents.size());
    }


    private void generateSimpleEvent() {
        eventPublisher.createSimpleEvent("simpleEvent");
    }

    private void generateAllPurchaseEvents() {
        generateCashPurchaseEvent();
        generateCreditPurchaseEvent();
    }

    private void generateCreditPurchaseEvent() {
        eventPublisher.createCreditPurchaseEvent("Plane Tickets", "123456789", 25900L);
    }

    private void generateCashPurchaseEvent() {
        eventPublisher.createCashPurchaseEvent("Jeep Wrangler", 25000L);
    }

    private class DeadEventSubscriber {

        List<DeadEvent> deadEvents = Lists.newArrayList();

        @Subscribe
        public void handleDeadEvent(DeadEvent deadEvent) {
            deadEvents.add(deadEvent);
        }
    }
}
