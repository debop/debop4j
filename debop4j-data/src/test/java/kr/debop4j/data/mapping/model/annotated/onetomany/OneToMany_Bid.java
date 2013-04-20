package kr.debop4j.data.mapping.model.annotated.onetomany;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.jpa.domain.JpaEntityBase;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * org.annotated.mapping.domain.model.onetomany.OneToMany_Bid
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 12. 4.
 */
@Entity
@Table(name = "JPA_ONE_TO_MANY_BID")
@DynamicInsert
@DynamicUpdate
public class OneToMany_Bid extends JpaEntityBase {

    private static final long serialVersionUID = 2401312144738936609L;

    @Id
    @GeneratedValue
    @Column(name = "BID_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID", nullable = false)
    private OneToMany_Item item;

    @Basic
    @Column(name = "AMOUNT", nullable = false)
    private BigDecimal amount;

    // Mapping 하지 않을 정보
    @Transient
    private Timestamp timestamp;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);

        return HashTool.compute(id);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("amount", amount)
                .add("item", item);
    }
}
