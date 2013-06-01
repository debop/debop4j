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

package kr.debop4j.core.pool;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * Apache common pool의 Generic Pool (Jedis 의 JedisPool 를 참고하여 만들었음)
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 8. 오전 12:41
 */
@Slf4j
@SuppressWarnings( "unchecked" )
public abstract class AbstractPool<T> implements AutoCloseable {

    protected GenericObjectPool pool;

    protected AbstractPool() { }

    public AbstractPool(final GenericObjectPool.Config poolConfig, PoolableObjectFactory<T> factory) {
        log.info("새로운 pool 을 생성합니다... config=[{}]", poolConfig);

        pool = new GenericObjectPool<T>(factory, poolConfig);
    }

    /** 풀에서 리소스를 얻습니다. */
    public T getResource() {
        if (log.isTraceEnabled())
            log.trace("Pool에서 resource 를 얻습니다...");

        try {
            T result = (T) pool.borrowObject();

            if (log.isTraceEnabled())
                log.trace("Pool에서 resource 를 얻었습니다. resource=[{}]", result);

            return result;

        } catch (Exception e) {
            log.error("Pool에서 리소스를 획득하는데 실패했습니다.", e);
            throw new RuntimeException(e);
        }
    }

    /** 재사용을 위해 풀에 리소스를 반환합니다. */
    public void returnResource(final T resource) {
        returnResourceObject(resource);
    }

    @SuppressWarnings("unchecked")
    protected void returnResourceObject(final Object resource) {
        if (log.isTraceEnabled())
            log.trace("Pool에 resource 를 반환합니다... resource=[{}]", resource);
        try {
            pool.returnObject(resource);
        } catch (Exception e) {
            log.error("리소스를 Pool로 반환하는데 실패했습니다. resource=" + resource, e);
            throw new RuntimeException(e);
        }
    }

    /** 재사용할 수 없는 리소스는 폐기하도록 합니다. */
    public void returnBrokenResource(final T resource) {
        returnBrokenResourceObject(resource);
    }

    @SuppressWarnings("unchecked")
    protected void returnBrokenResourceObject(final Object resource) {
        try {
            pool.invalidateObject(resource);
        } catch (Exception e) {
            log.error("더이상 사용할 수 없는 리소스를 제거하는데 실패했습니다.", e);
            throw new RuntimeException(e);
        }
    }

    /** 풀을 제거합니다. 내부의 남아있는 모든 리소스를 제거합니다. */
    public void destroy() {
        if (log.isTraceEnabled()) log.trace("Pool을 제거합니다...");
        try {
            pool.close();
            pool = null;
        } catch (Exception e) {
            log.error("pool을 제거하는데 실패했습니다.", e);
            throw new RuntimeException(e);
        }
        log.info("Pool을 제거했습니다.");
    }

    public void close() throws Exception {
        destroy();
    }
}
