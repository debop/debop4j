package kr.debop4j.core.cache.couchbase;

import com.couchbase.client.CouchbaseClient;
import com.google.common.collect.Lists;
import kr.debop4j.core.Guard;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.spy.memcached.internal.OperationFuture;
import org.springframework.cache.support.SimpleValueWrapper;

import java.net.URI;
import java.util.List;

/**
 * Couchbase를 캐시 저장소로 사용하는 Cache
 * 참고: http://techstickynotes.blogspot.kr/2012/04/spring-cache-couchbase-nosql-db.html
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 24. 오후 6:03
 */
@Slf4j
public class CouchbaseCache implements org.springframework.cache.Cache {

    public static final int EXP_TIME = 100000;

    @Getter
    private String name;
    @Getter
    private CouchbaseClient nativeCache = null;

    public CouchbaseCache(String name) {
        if (CouchbaseCache.log.isDebugEnabled())
            CouchbaseCache.log.debug("CouchbaseCache 를 생성합니다. name=[{}]", name);
        this.name = name;
        init();
    }

    protected void init() {
        try {
            List<URI> uris = Lists.newLinkedList();
            uris.add(new URI("http://127.0.0.1:8091/pools"));
        } catch (Exception e) {
            CouchbaseCache.log.error("Couchbase 서버에 연결하는데 실패했습니다.", e);
        }
    }

    @Override
    public ValueWrapper get(Object key) {
        Guard.shouldNotBeNull(key, "key");

        Object result = null;
        if (key instanceof String) {
            result = nativeCache.get((String) key);
        } else {
            CouchbaseCache.log.error("Invalid key type: " + key.getClass());
        }
        SimpleValueWrapper wrapper = null;
        if (result != null)
            wrapper = new SimpleValueWrapper(result);
        return wrapper;
    }

    @Override
    public void put(Object key, Object value) {
        Guard.shouldNotBeNull(key, "key");
        if (!(key instanceof String)) {
            CouchbaseCache.log.error("Invalid key type: " + key.getClass());
            return;
        }

        OperationFuture<Boolean> setOp = null;
        ValueWrapper cached = get(key);
        if (cached == null)
            setOp = nativeCache.add((String) key, EXP_TIME, value);
        else
            setOp = nativeCache.replace((String) key, EXP_TIME, value);

        if (CouchbaseCache.log.isInfoEnabled()) {
            if (setOp.getStatus().isSuccess()) {
                CouchbaseCache.log.info("객체를 캐시 키[{}]로 저장했습니다.", key);
            } else {
                CouchbaseCache.log.info("객체를 캐시 키[{}]로 저장하는데 실패했습니다. operation=[{}]", key, setOp);
            }
        }
    }

    @Override
    public void evict(Object key) {
        // Nothing to do
    }

    @Override
    public void clear() {
        // Nothing to do
    }
}
