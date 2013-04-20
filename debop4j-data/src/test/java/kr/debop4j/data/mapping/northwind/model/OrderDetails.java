package kr.debop4j.data.mapping.northwind.model;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedEntityBase;
import kr.debop4j.data.model.IEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * kr.debop4j.data.mapping.northwind.model.OrderDetails
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 2. 23.
 */
@Entity
@Table(name = "`Order Details`")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class OrderDetails extends AnnotatedEntityBase implements IEntity<OrderDetailsIdentifier> {

    protected OrderDetails() {}

    public OrderDetails(Order order, Product product) {
        id = new OrderDetailsIdentifier(order, product);
    }

    // Composite Id
    @EmbeddedId
    private OrderDetailsIdentifier id;

    private BigDecimal unitPrice;
    private Integer quantity;
    private Float discount;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);

        return HashTool.compute();
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("unitPrice", unitPrice)
                .add("quantity", quantity)
                .add("discount", discount);
    }
}
