package kr.debop4j.data.mapping.northwind.model;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
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
import java.math.BigDecimal;
import java.util.Set;

/**
 * kr.debop4j.data.mapping.northwind.model.Product
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 2. 23.
 */
@Entity
@Table(name = "Products")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Product extends AnnotatedEntityBase implements IEntity<Integer> {

    protected Product() {}

    public Product(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ProductId")
    private Integer id;

    @Column(name = "ProductName")
    private String name;

    private String quantityPerUnit;
    private BigDecimal unitPrice;
    private Integer unitsInStock;
    private Integer unitsOnOrder;
    private Integer reorderLevel;
    private Boolean discontinued;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    private Category category;

    @OneToMany(mappedBy = "id.product", cascade = { CascadeType.ALL }, orphanRemoval = true)
    @LazyCollection(value = LazyCollectionOption.EXTRA)
    private Set<OrderDetails> orderDetails = Sets.newHashSet();


    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(name);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("name", name)
                .add("unitPrice", unitPrice);
    }
}
