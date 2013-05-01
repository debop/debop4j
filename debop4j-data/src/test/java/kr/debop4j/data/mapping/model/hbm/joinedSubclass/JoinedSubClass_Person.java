package kr.debop4j.data.mapping.model.hbm.joinedSubclass;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.EntityBase;
import lombok.Getter;
import lombok.Setter;

/**
 * org.hibernate.mapping.domain.model.joinedSubclass.JoinedSubClass_Person
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 2.
 */
public abstract class JoinedSubClass_Person extends EntityBase<Long> {

    private static final long serialVersionUID = -7965446416537277249L;

    @Getter
    @Setter
    private JoinedSubClass_Company company;

    @Getter
    @Setter
    private String name;

    @Override
    public int hashCode() {
        return isPersisted() ? super.hashCode()
                : HashTool.compute(name);

    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("name", name);
    }
}
