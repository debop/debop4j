package kr.debop4j.core;

import com.google.common.base.Objects;

/**
 * Value Object (Component) 의 최상위 추상화 클래스입니다.
 * JpaUser: sunghyouk.bae@gmail.com
 * Date: 12. 11. 19
 */
public abstract class ValueObjectBase implements IValueObject {

    @Override
    public boolean equals(Object obj) {
        return obj == this ||
                (obj != null &&
                        getClass() == obj.getClass() &&
                        hashCode() == obj.hashCode());
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }

    @Override
    public String toString() {
        return this.buildStringHelper().toString();
    }

    /**
     * {@link ValueObjectBase#toString()}을 재정의하지 말고, buildStringHelper를 재정의 하세요.
     */
    protected Objects.ToStringHelper buildStringHelper() {
        return Objects.toStringHelper(this);
    }
}
