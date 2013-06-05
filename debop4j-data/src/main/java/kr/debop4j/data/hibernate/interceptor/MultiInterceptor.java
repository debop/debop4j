/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.debop4j.data.hibernate.interceptor;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Interceptor;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.FutureTask;

/**
 * 복수 개의 Interceptor들을 모두 수행하도록 하는 Interceptor입니다. (기본적으로 Interceptor를 하나만 등록하게 되어 있어서)
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 23.
 */
public class MultiInterceptor extends EmptyInterceptor {

    private static final Logger log = LoggerFactory.getLogger(MultiInterceptor.class);
    private static final boolean isTraceEnabled = log.isTraceEnabled();
    private static final long serialVersionUID = 4354823013240170534L;

    @Getter
    @Setter
    private List<Interceptor> interceptors = Lists.newArrayList();

    /** Instantiates a new Multi interceptor. */
    public MultiInterceptor() { }

    /**
     * Instantiates a new Multi interceptor.
     *
     * @param interceptors the interceptors
     */
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

        if (isTraceEnabled)
            log.trace("인터셉터의 onDelete메소드를 멀티캐스트로 수행합니다.");

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

        if (isTraceEnabled)
            log.trace("인터셉터의 onFlush 메소드를 멀티캐스트로 수행합니다.");

        for (final Interceptor interceptor : interceptors) {
            interceptor.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
        }

        return false;
    }

    @Override
    public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (!isExists())
            return false;

        if (isTraceEnabled)
            log.trace("인터셉터의 onLoad 메소드를 멀티캐스트로 수행합니다.");

        for (final Interceptor interceptor : interceptors) {
            interceptor.onLoad(entity, id, state, propertyNames, types);
        }

        return false;
    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (!isExists())
            return false;

        if (isTraceEnabled)
            log.trace("인터셉터의 onSave 메소드를 멀티캐스트로 수행합니다.");

        List<FutureTask<Boolean>> tasks = Lists.newLinkedList();

        for (final Interceptor interceptor : interceptors) {
            interceptor.onSave(entity, id, state, propertyNames, types);
        }

        return false;
    }

    @Override
    public void postFlush(Iterator entities) {
        if (!isExists()) return;

        if (isTraceEnabled)
            log.trace("인터셉터의 postFlush 메소드를 멀티캐스트로 수행합니다.");

        List<FutureTask<Void>> tasks = Lists.newLinkedList();

        for (final Interceptor interceptor : interceptors) {
            interceptor.postFlush(entities);
        }
    }

    @Override
    public void preFlush(Iterator entities) {
        if (!isExists()) return;

        if (isTraceEnabled)
            log.trace("인터셉터의 preFlush 메소드를 멀티캐스트로 수행합니다.");

        List<FutureTask<Void>> tasks = Lists.newLinkedList();

        for (final Interceptor interceptor : interceptors) {
            interceptor.preFlush(entities);
        }
    }
}
