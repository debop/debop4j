package kr.debop4j.data.mapping.model.hbm.joinedSubclass;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.EntityBase;
import lombok.Getter;
import lombok.Setter;

/**
 * org.hibernate.mapping.domain.model.joinedSubclass.JoinedSubClass_Company.hbm.xml
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 2.
 */
public class JoinedSubClass_Company extends EntityBase<Long> {

    private static final long serialVersionUID = 8028783232908962980L;

    @Getter
    @Setter
    private String name;


    @Override
    public int hashCode() {
        if (isPersisted())
            return super.hashCode();

        return HashTool.compute(name);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("name", name);
    }
}
