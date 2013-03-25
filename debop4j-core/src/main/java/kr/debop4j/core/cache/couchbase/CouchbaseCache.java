package kr.debop4j.core.cache.couchbase;

import com.couchbase.client.CouchbaseClient;
import kr.debop4j.core.Guard;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.spy.memcached.internal.OperationFuture;
import org.springframework.cache.support.SimpleValueWrapper;

/**
 * Couchbase를 캐시 저장소로 사용하는 Cache
 * 참고: http://techstickynotes.blogspot.kr/2012/04/spring-cache-couchbase-nosql-db.html
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 24. 오후 6:03
 */
@Slf4j
public class CouchbaseCache implements org.springframework.cache.Cache {

    @Getter
    private String name;
    @Getter
    private CouchbaseClient nativeCache = null;

    @Getter
    @Setter
    private int expireTime = 600_000; // msec

    public CouchbaseCache(String name, CouchbaseClient client) {
        Guard.shouldNotBeNull(name, "name");
        Guard.shouldNotBeNull(client, "client");

        this.name = name;
        this.nativeCache = client;

        if (log.isDebugEnabled())
            log.debug("CouchbaseCache 를 생성합니다. name=[{}], nativeCache=[{}]", name, nativeCache);
    }

    @Override
    public ValueWrapper get(Object key) {
        Guard.shouldNotBeNull(key, "key");

        Object result = nativeCache.get(key.toString());

        SimpleValueWrapper wrapper = null;
        if (result != null)
            wrapper = new SimpleValueWrapper(result);

        return wrapper;
    }

    @Override
    public void put(Object key, Object value) {
        Guard.shouldNotBeNull(key, "key");
        if (!(key instanceof String)) {
            log.error("Invalid key type: " + key.getClass());
            return;
        }

        OperationFuture<Boolean> setOp = null;
//        ValueWrapper cached = get(key);
//        if (cached == null)
//            setOp = nativeCache.add((String) key, expireTime, value);
//        else
//            setOp = nativeCache.replace((String) key, expireTime, value);

        setOp = nativeCache.set(key.toString(), expireTime, value);

        if (setOp.getStatus().isSuccess()) {
            if (log.isDebugEnabled())
                log.debug("객체를 캐시 키[{}]로 저장했습니다.", key);
        } else {
            if (log.isDebugEnabled())
                log.debug("객체를 캐시 키[{}]로 저장하는데 실패했습니다. operation=[{}]", key, setOp);
        }
    }

    @Override
    public void evict(Object key) {
        Guard.shouldNotBeNull(key, "key");
        if (log.isDebugEnabled())
            log.debug("delete cache item... key=[{}]", key);
        nativeCache.delete(key.toString());
    }

    @Override
    public void clear() {
        if (log.isDebugEnabled())
            log.debug("clear cache...");
        nativeCache.flush();
    }
}
