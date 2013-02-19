package kr.debop4j.data.mapping.model.hbm.joinedSubclass;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * org.hibernate.mapping.domain.model.joinedSubclass.JoinedSubClass_Customer
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 2.
 */
public class JoinedSubClass_Customer extends JoinedSubClass_Person {

    private static final long serialVersionUID = 8834743565965707790L;

    @Getter
    @Setter
    private JoinedSubClass_Employee contactOwner;


    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("contactOwner", contactOwner);
    }
}
