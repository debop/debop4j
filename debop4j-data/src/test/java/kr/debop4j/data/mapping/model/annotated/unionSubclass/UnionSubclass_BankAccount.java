package kr.debop4j.data.mapping.model.annotated.unionSubclass;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * org.annotated.mapping.domain.model.subclass.UnionSubclass_BankAccount
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 4.
 */
@Entity
@Table(name = "JPA_UNION_SUBCLASS_BANK_ACCOUNT")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class UnionSubclass_BankAccount extends UnionSubclass_BillingDetails {

    private static final long serialVersionUID = 6159765179966313199L;

    @Column(name = "BANK_ACCOUNT", nullable = false)
    private String account;

    @Column(name = "BANK_NAME", nullable = false)
    private String bankname;

    @Column(name = "BANK_SWIFT")
    private String swift;

    @Override
    public int hashCode() {
        if (isPersisted())
            return super.hashCode();
        return HashTool.compute(super.hashCode(), account, bankname);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("account", account)
                .add("bankname", bankname)
                .add("swift", swift);
    }
}
