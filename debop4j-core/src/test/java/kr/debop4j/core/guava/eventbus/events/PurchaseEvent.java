package kr.debop4j.core.guava.eventbus.events;

import com.google.common.base.Objects;
import lombok.Getter;

/**
 * kr.debop4j.core.guava.eventbus.events.PurchaseEvent
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 10.
 */
public class PurchaseEvent {

    @Getter
    protected long amount;

    public PurchaseEvent(long amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("amount", amount)
                .toString();
    }
}
