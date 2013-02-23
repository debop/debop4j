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
import java.util.List;

/**
 * kr.debop4j.data.mapping.northwind.model.Customer
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 23.
 */
@Entity
@Table(name = "Customers")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Customer extends AnnotatedEntityBase implements IEntity<String> {

    protected Customer() {}

    public Customer(String customerId) {
        this.id = customerId;
    }

    @Id
    @Column(name = "customerId", length = 5)
    private String id;

    private String companyName;
    private String contactName;

    @OneToMany(mappedBy = "orderedBy", cascade = {CascadeType.ALL})
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<Order> orders = Lists.newArrayList();

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);

        return HashTool.compute(companyName, contactName);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("companyName", companyName)
                .add("contactName", contactName);
    }
}
