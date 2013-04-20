package kr.debop4j.data.hibernate.config.java;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * kr.debop4j.data.hibernate.config.java.Account
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 2. 19.
 */
@Entity
@Table(name = "T_ACCOUNT")
@Getter
@Setter
public class Account extends AnnotatedEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.PRIVATE)
    private long id;

    private double cashBalance;

    private String name;

    @Override
    public int hashCode() {
        if (id != 0)
            return (int) id;

        return HashTool.compute(cashBalance, name);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("balance", cashBalance)
                .add("name", name);
    }
}
