package kr.debop4j.data.mapping.model.annotated.subclass;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.jpa.domain.JpaEntityBase;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * org.annotated.mapping.domain.model.subclass.Subclass_BillingDetails
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 12. 4.
 */
@Entity
@Table(name = "JPA_SUBCLASS_BILLING_DETAILS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "BILLING_TYPE", discriminatorType = DiscriminatorType.STRING)
@DynamicInsert
@DynamicUpdate
public class Subclass_BillingDetails extends JpaEntityBase {

    private static final long serialVersionUID = -972548475181911220L;

    @Id
    @GeneratedValue
    @Column(name = "BILLING_DETAILS_ID")
    private Long id;

    @Column(name = "OWNER", nullable = false)
    private String owner;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(owner);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("owner", owner);
    }
}
