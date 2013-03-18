package kr.debop4j.data.model;

import com.google.common.base.Objects;
import kr.debop4j.core.ValueObjectBase;

/**
 * Hibernate 저장 상태를 표현하는 추상화 클래스입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 11. 19
 */
public abstract class StatefulEntityBase extends ValueObjectBase implements IStatefulEntity {

    private boolean persisted = false;

    @Override
    public boolean isPersisted() {
        return persisted;
    }

    @Override
    public void onSave() {
        persisted = true;
    }

    @Override
    public void onPersist() {
        persisted = true;
    }

    @Override
    public void onLoad() {
        persisted = true;
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                    .add("persisted", persisted);
    }
}
