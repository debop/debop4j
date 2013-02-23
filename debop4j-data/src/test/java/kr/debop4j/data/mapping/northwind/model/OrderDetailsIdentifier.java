package kr.debop4j.data.mapping.northwind.model;

import com.google.common.base.Objects;
import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * kr.debop4j.data.mapping.northwind.model.OrderDetailsIdentifier
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 23.
 */
@Embeddable
@Getter
@Setter
public class OrderDetailsIdentifier extends ValueObjectBase {

    protected OrderDetailsIdentifier() {}

    public OrderDetailsIdentifier(Order order, Product product) {
        this.order = order;
        this.product = product;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "orderId")
    private Order order;

    @ManyToOne(optional = false)
    @JoinColumn(name = "productId")
    private Product product;

    @Override
    public int hashCode() {
        return HashTool.compute(order, product);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("order", order)
                .add("product", product);
    }
}
