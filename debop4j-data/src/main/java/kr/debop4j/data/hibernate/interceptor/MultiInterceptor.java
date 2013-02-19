package kr.debop4j.data.hibernate.interceptor;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Interceptor;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.FutureTask;

/**
 * 복수 개의 Interceptor들을 병렬 방식으로 수행하도록 하는 Interceptor입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 23.
 */
@Slf4j
public class MultiInterceptor extends EmptyInterceptor {

    private static final long serialVersionUID = 4354823013240170534L;

    @Getter
    @Setter
    private List<Interceptor> interceptors;

    public MultiInterceptor() { }

    public MultiInterceptor(List<Interceptor> interceptors) {
        this.interceptors = interceptors;
    }

    private boolean isExists() {
        return (interceptors != null && interceptors.size() > 0);
    }

    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {

        if (!isExists())
            return;

        if (log.isDebugEnabled())
            log.debug("인터셉터의 onDelete메소드를 멀티캐스트로 수행합니다.");

        for (final Interceptor interceptor : interceptors) {
            interceptor.onDelete(entity, id, state, propertyNames, types);
        }
    }

    @Override
    public boolean onFlushDirty(Object entity,
                                Serializable id,
                                Object[] currentState,
                                Object[] previousState,
                                String[] propertyNames,
                                Type[] types) {
        if (!isExists())
            return false;

        if (log.isDebugEnabled())
            log.debug("인터셉터의 onFlush 메소드를 멀티캐스트로 수행합니다.");

        for (final Interceptor interceptor : interceptors) {
            interceptor.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
        }

        return false;
    }

    @Override
    public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (!isExists())
            return false;

        if (log.isDebugEnabled())
            log.debug("인터셉터의 onLoad 메소드를 멀티캐스트로 수행합니다.");

        for (final Interceptor interceptor : interceptors) {
            interceptor.onLoad(entity, id, state, propertyNames, types);
        }

        return false;
    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (!isExists())
            return false;

        if (log.isDebugEnabled())
            log.debug("인터셉터의 onSave 메소드를 멀티캐스트로 수행합니다.");

        List<FutureTask<Boolean>> tasks = Lists.newLinkedList();

        for (final Interceptor interceptor : interceptors) {
            interceptor.onSave(entity, id, state, propertyNames, types);
        }

        return false;
    }

    @Override
    public void postFlush(Iterator entities) {
        if (!isExists()) return;

        if (log.isDebugEnabled())
            log.debug("인터셉터의 postFlush 메소드를 멀티캐스트로 수행합니다.");

        List<FutureTask<Void>> tasks = Lists.newLinkedList();

        for (final Interceptor interceptor : interceptors) {
            interceptor.postFlush(entities);
        }
    }

    @Override
    public void preFlush(Iterator entities) {
        if (!isExists()) return;

        if (log.isDebugEnabled())
            log.debug("인터셉터의 preFlush 메소드를 멀티캐스트로 수행합니다.");

        List<FutureTask<Void>> tasks = Lists.newLinkedList();

        for (final Interceptor interceptor : interceptors) {
            interceptor.preFlush(entities);
        }
    }
}
