package kr.debop4j.data.couchbase.cache;

import com.couchbase.client.CouchbaseClient;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;

import java.util.Collection;

/**
 * Couchbase를 캐시 저장소로 사용하는 Spring Cache 용 CacheManager 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 24. 오후 6:00
 */
@Slf4j
public class CouchbaseCacheManager extends AbstractTransactionSupportingCacheManager {

    private CouchbaseClient couchbaseClient;

    @Getter
    @Setter
    private int expireMillis = 0;  // 0 : persist forever

    public CouchbaseCacheManager(CouchbaseClient couchbaseClient) {
        this(couchbaseClient, 0);
    }

    public CouchbaseCacheManager(CouchbaseClient couchbaseClient, int expireMillis) {
        assert couchbaseClient != null;

        this.couchbaseClient = couchbaseClient;
        this.expireMillis = expireMillis;
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        Collection<Cache> caches = Lists.newArrayList();

        for (String name : getCacheNames()) {
            caches.add(new CouchbaseCache(name, couchbaseClient, expireMillis));
        }
        return caches;
    }

    @Override
    public Cache getCache(String name) {
        synchronized (this) {
            Cache cache = super.getCache(name);
            if (cache == null) {
                cache = new CouchbaseCache(name, couchbaseClient, expireMillis);
                addCache(cache);
            }
            return cache;
        }
    }
}
