package kr.debop4j.data.mapping.model.annotated.unionSubclass;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * org.annotated.mapping.domain.model.subclass.UnionSubclass_CreditCard
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 4.
 */
@Getter
@Setter
@Entity
@Table(name = "JPA_UNION_SUBCLASS_CREDIT_CARD")
@DynamicInsert
@DynamicUpdate
@org.hibernate.annotations.Check(constraints = "START_DATE <= END_DATE")
public class UnionSubclass_CreditCard extends UnionSubclass_BillingDetails {

    private static final long serialVersionUID = -8344985908332422466L;

    @Column(name = "CC_NUMBER", nullable = false)
    private String number;

    @Column(name = "CC_EXP_MONTH", nullable = false, length = 2)
    private String expMonth;

    @Column(name = "CC_EXP_YEAR", nullable = false, length = 4)
    private String expYear;

    @Temporal(TemporalType.DATE)
    @Column(name = "START_DATE")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE")
    private Date endDate;

    @Override
    public int hashCode() {
        if (isPersisted())
            return super.hashCode();

        return HashTool.compute(super.hashCode(), number, expMonth, expYear);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("number", number)
                .add("expMonth", expMonth)
                .add("expYear", expYear)
                .add("startDate", startDate)
                .add("endDate", endDate);
    }
}
