package kr.debop4j.data.memcached;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.GetFuture;
import net.spy.memcached.internal.OperationFuture;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

/**
 * Memcached를 캐시 저장소로 사용하는 Cache
 *
 * @author sunghyouk.bae@gmail.com
 *         Date: 13. 3. 25 오전 11:42
 */
@Slf4j
public class MemcachedCache implements Cache {

    @Getter
    private String name = "default";
    @Getter
    private MemcachedClient nativeCache = null;

    @Getter
    private int expireSeconds;

    public MemcachedCache(MemcachedClient client) {
        this(client, 26 * 60 * 60);
    }

    public MemcachedCache(MemcachedClient client, int expireSeconds) {
        assert client != null;

        this.nativeCache = client;
        this.expireSeconds = expireSeconds;
        if (log.isDebugEnabled())
            log.debug("MemcachedCache를 생성했습니다");
    }

    @Override
    public ValueWrapper get(Object key) {
        assert key != null;

        GetFuture<Object> result = nativeCache.asyncGet(key.toString());

        SimpleValueWrapper wrapper = null;
        try {
            if (result.get() != null)
                wrapper = new SimpleValueWrapper(result.get());
        } catch (Exception ignored) {}
        return wrapper;
    }

    @Override
    public void put(Object key, Object value) {
        assert key != null;

        OperationFuture<Boolean> setOp = null;
        setOp = nativeCache.set(key.toString(), expireSeconds, value);

        if (log.isInfoEnabled()) {
            if (setOp.getStatus().isSuccess()) {
                log.info("객체를 캐시 키[{}]로 저장했습니다.", key);
            } else {
                log.info("객체를 캐시 키[{}]로 저장하는데 실패했습니다. operation=[{}]", key, setOp);
            }
        }
    }

    @Override
    public void evict(Object key) {
        if (key != null)
            nativeCache.delete(key.toString());
    }

    @Override
    public void clear() {
        nativeCache.flush();
    }
}
