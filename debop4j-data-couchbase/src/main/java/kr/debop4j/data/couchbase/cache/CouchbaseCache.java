package kr.debop4j.data.couchbase.cache;

import com.couchbase.client.CouchbaseClient;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.spy.memcached.internal.OperationFuture;
import org.springframework.cache.support.SimpleValueWrapper;

/**
 * Couchbase를 캐시 저장소로 사용하는 Cache
 * 참고: http://techstickynotes.blogspot.kr/2012/04/spring-cache-couchbase-data-db.html
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
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
    private int expireMillis = 0; // msec (0 is persist forever)

    public CouchbaseCache(String name, CouchbaseClient client, int expireMillis) {
        this.name = name;
        this.nativeCache = client;
        this.expireMillis = expireMillis;

        if (log.isDebugEnabled())
            log.debug("CouchbaseCache 를 생성합니다. name=[{}], nativeCache=[{}]", name, nativeCache);
    }

    private String getKey(Object key) {
        return name + "|+|" + key;
    }

    @Override
    public ValueWrapper get(Object key) {
        assert key != null;

        Object result = nativeCache.get(getKey(key));

        SimpleValueWrapper wrapper = null;
        if (result != null)
            wrapper = new SimpleValueWrapper(result);

        return wrapper;
    }

    @Override
    public void put(Object key, Object value) {
        assert key != null;

        OperationFuture<Boolean> setOp = nativeCache.set(getKey(key), expireMillis, value);

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
        assert key != null;
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
