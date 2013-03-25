package kr.debop4j.core.cache.couchbase;

import com.couchbase.client.CouchbaseClient;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.List;

/**
 * Couchbase를 캐시 저장소로 사용하는 Spring Cache 용 CacheManager 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 24. 오후 6:00
 */
@Slf4j
public class CouchbaseCacheManager extends AbstractTransactionSupportingCacheManager {

    private List<URI> couchbaseUris;
    private String password;

    public CouchbaseCacheManager(List<URI> couchbaseUris, String passwd) {

        this.couchbaseUris = couchbaseUris;
        this.password = password;
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        Collection<Cache> caches = Lists.newArrayList();

        for (String name : getCacheNames()) {
            caches.add(new CouchbaseCache(name, createCouchbaseClient(name)));
        }
        return caches;
    }

    @Override
    public Cache getCache(String name) {
        synchronized (this) {
            Cache cache = super.getCache(name);
            if (cache == null) {
                cache = new CouchbaseCache(name, createCouchbaseClient(name));
                addCache(cache);
            }
            return cache;
        }
    }

    protected CouchbaseClient createCouchbaseClient(String bucketName) {
        try {
            return new CouchbaseClient(couchbaseUris, bucketName, password);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
