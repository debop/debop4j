package kr.debop4j.data.mapping.northwind.model;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedEntityBase;
import kr.debop4j.data.model.IEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * kr.debop4j.data.mapping.northwind.model.Order
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 23.
 */
@Entity
@Table(name = "Orders")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Order extends AnnotatedEntityBase implements IEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "orderId")
    private Integer id;

    private Date orderDate;
    private Date requiredDate;
    private Date shippedDate;
    private String shipName;

    @ManyToOne
    @JoinColumn(name = "employeeId", nullable = false)
    private Employee seller;

    @ManyToOne
    @JoinColumn(name = "customerId", nullable = false)
    private Customer orderedBy;

    @OneToMany(mappedBy = "id.order", cascade = {CascadeType.ALL}, orphanRemoval = true)
    @LazyCollection(value = LazyCollectionOption.EXTRA)
    private List<OrderDetails> orderDetails = Lists.newArrayList();

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(orderDate, requiredDate, shippedDate, shipName);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("orderDate", orderDate)
                .add("requiredDate", requiredDate)
                .add("shippedDate", shippedDate)
                .add("shipName", shipName);
    }
}
