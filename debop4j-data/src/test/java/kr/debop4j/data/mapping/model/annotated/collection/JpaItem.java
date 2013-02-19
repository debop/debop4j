package kr.debop4j.data.mapping.model.annotated.collection;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.jpa.domain.JpaEntityBase;
import kr.debop4j.data.mapping.model.hbm.collection.ItemState;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * org.annotated.mapping.domain.model.collection.JpaItem
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 2.
 */
@Entity
@Table(name = "JPA_ITEM")
@Getter
@Setter
public class JpaItem extends JpaEntityBase {

    private static final long serialVersionUID = -4178978313674407260L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ITEM_ID")
    private Long id;

    private String name;
    private String description;
    private BigDecimal initialPrice;
    private BigDecimal reservePrice;
    private Date startDate;
    private Date endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "ITEM_STATE", nullable = false, length = 128)
    private ItemState state;

    private Date approvalDate;

    @CollectionTable(name = "JPA_COL_ITEM_IMGS", joinColumns = @JoinColumn(name = "ITEM_ID"))
    @ElementCollection(targetClass = JpaImage.class)
    private Set<JpaImage> images = new HashSet<>();

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
                .add("description", description)
                .add("initialPrice", initialPrice)
                .add("reservePrice", reservePrice)
                .add("startDate", startDate)
                .add("endDate", endDate)
                .add("state", state)
                .add("approvalDate", approvalDate);
    }
}
