package kr.debop4j.data.couchbase.spring.cache;

import com.couchbase.client.CouchbaseClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Couchbase 를 캐시 저장소로 사용하는 Spring {@link org.springframework.cache.CacheManager}의 구현체입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 3. 오전 11:30
 */
@Slf4j
public class CouchbaseCacheManager implements CacheManager {

    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();
    private final Collection<String> names = Collections.unmodifiableCollection(caches.keySet());
    private final CouchbaseClient client;

    // 0 - never expire (in seconds)
    private int defaultExpiration = 300;
    private Map<String, Integer> expires = null;

    public CouchbaseCacheManager(CouchbaseClient client) {
        this(client, 300);
    }

    public CouchbaseCacheManager(CouchbaseClient client, int expiration) {
        this.client = client;
        this.defaultExpiration = expiration;
    }

    @Override
    public Cache getCache(String name) {
        if (CouchbaseCacheManager.log.isTraceEnabled()) CouchbaseCacheManager.log.trace("Cache를 구합니다. name=[{}]", name);

        if (!caches.containsKey(name)) {
            int expiration = computeExpiration(name);
            CouchbaseCache cache = new CouchbaseCache(name, client, expiration);
            caches.putIfAbsent(name, cache);
        }
        return caches.get(name);
    }

    @Override
    public Collection<String> getCacheNames() {
        return names;
    }

    private int computeExpiration(String name) {
        Integer expiration = null;
        if (expires != null) {
            expiration = expires.get(name);
        }
        return (expiration != null) ? expiration : defaultExpiration;
    }

    /**
     * 캐시의 기본 유효 시간을 설정합니다. (in seconds)
     *
     * @param defaultExpiration time in seconds
     */
    public void setDefaultExpiration(int defaultExpiration) {
        this.defaultExpiration = defaultExpiration;
    }

    /** 캐시별 유효 시간을 설정합니다. (in seconds) */
    public void setExpires(Map<String, Integer> expires) {
        this.expires = (expires != null) ? new ConcurrentHashMap<String, Integer>(expires) : null;
    }
}
