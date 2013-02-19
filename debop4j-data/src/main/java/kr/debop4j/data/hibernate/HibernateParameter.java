package kr.debop4j.data.hibernate;

import com.google.common.base.Objects;
import kr.debop4j.data.NamedParameterBase;
import lombok.Getter;

/**
 * Hibernate용 Parameter 정보를 표현합니다.
 * JpaUser: sunghyouk.bae@gmail.com
 * Date: 12. 11. 19
 */
public class HibernateParameter extends NamedParameterBase {

    private static final long serialVersionUID = -6291985997768450558L;

    @Getter
    private org.hibernate.type.Type type;

    public HibernateParameter(String name, Object value) {
        super(name, value);
    }

    public HibernateParameter(String name, Object value, org.hibernate.type.Type type) {
        super(name, value);
        this.type = type;
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("type", type);
    }
}
