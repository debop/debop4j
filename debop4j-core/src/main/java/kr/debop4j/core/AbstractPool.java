package kr.debop4j.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * Generic Pool (Jedis 의 JedisPool 를 참고하여 만들었음)
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 8. 오전 12:41
 */
@Slf4j
public abstract class AbstractPool<T> implements AutoCloseable {

    protected GenericObjectPool pool;

    protected AbstractPool() { }

    public AbstractPool(final GenericObjectPool.Config poolConfig, PoolableObjectFactory<T> factory) {
        pool = new GenericObjectPool<T>(factory, poolConfig);
    }

    @SuppressWarnings("unchecked")
    public T getResource() {
        if (log.isDebugEnabled())
            log.debug("Pool에서 resource 를 얻습니다...");
        try {
            return (T) pool.borrowObject();
        } catch (Exception e) {
            log.error("Pool에서 리소스를 획득하는데 실패했습니다.", e);
            throw new RuntimeException(e);
        }
    }

    public void returnResource(final T resource) {
        returnResourceObject(resource);
    }

    @SuppressWarnings("unchecked")
    protected void returnResourceObject(final Object resource) {
        if (log.isDebugEnabled())
            log.debug("Pool에 resource 를 반환합니다. resource=[{}]", resource);
        try {
            pool.returnObject(resource);
        } catch (Exception e) {
            log.error("리소스를 Pool로 반환하는데 실패했습니다. resource=" + resource, e);
            throw new RuntimeException(e);
        }
    }

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

    public void destroy() {
        if (log.isDebugEnabled())
            log.debug("Pool 을 제거합니다...");
        try {
            pool.close();
            pool = null;
        } catch (Exception e) {
            log.error("pool 을 제거하는데 실패했습니다.", e);
            throw new RuntimeException(e);
        }
        if (log.isDebugEnabled())
            log.debug("Pool을 제거했습니다.");
    }

    public void close() throws Exception {
        destroy();
    }
}
