package kr.debop4j.core.guava.eventbus.events;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * kr.nsoft.commons.guava.eventbus.events.CashPurchaseEvent
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 10.
 */
@Slf4j
public class CashPurchaseEvent extends PurchaseEvent {

    @Getter
    private String item;

    public CashPurchaseEvent(long amount, String item) {
        super(amount);
        this.item = item;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("amount", amount)
                .add("item", item)
                .toString();
    }
}
