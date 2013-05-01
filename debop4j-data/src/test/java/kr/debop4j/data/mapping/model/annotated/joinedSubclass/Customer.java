package kr.debop4j.data.mapping.model.annotated.joinedSubclass;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * org.annotated.mapping.domain.model.joinedSubclass.Customer
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 8.
 */
@Entity
@Table(name = "JS_CUSTOMER")
public class Customer extends PersonBase {

    private static final long serialVersionUID = -2249084619324711936L;

    @ManyToOne
    @JoinColumn(name = "CONTACT_EMP_ID", nullable = false)
    private Employee contactEmployee;

    @Override
    public int hashCode() {
        if (isPersisted())
            return super.hashCode();

        return HashTool.compute(super.hashCode(), contactEmployee);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("contactEmployee", contactEmployee);
    }
}
