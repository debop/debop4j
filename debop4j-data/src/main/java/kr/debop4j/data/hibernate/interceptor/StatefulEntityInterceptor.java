package kr.debop4j.data.hibernate.interceptor;

import kr.debop4j.data.model.IStatefulEntity;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import java.io.Serializable;

/**
 * 엔티티의 상태 정보를 갱신하는 Interceptor입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 11. 21.
 */
@Slf4j
public class StatefulEntityInterceptor extends EmptyInterceptor {

    private static final long serialVersionUID = 6945967982003155513L;

    /**
     * 엔티티가 영구 저장소에 저장된 엔티티인지 여부를 반환합니다.
     */
    public Boolean isPersisted(Object entity) {
        if (entity instanceof IStatefulEntity)
            return ((IStatefulEntity) entity).isPersisted();
        return null;
    }

    /**
     * 엔티티를 저장소로부터 로드한 후에 발생하는 이벤트입니다. 엔티티의 isPersisted 상태를 true 로 설정합니다.
     */
    @Override
    public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (entity instanceof IStatefulEntity) {
            ((IStatefulEntity) entity).onLoad();
        }
        return false;
    }

    /**
     * 엔티티가 삭제된 후에 호출되는 메소드입니다. 엔티티의 isPersisted 상태를 false 로 설정합니다.
     */
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (entity instanceof IStatefulEntity) {
            ((IStatefulEntity) entity).onSave();
        }
        return false;
    }
}
