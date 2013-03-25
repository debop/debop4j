package kr.debop4j.core.cache.couchbase;

import com.google.common.collect.Sets;
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

    @Override
    protected Collection<? extends Cache> loadCaches() {

        Collection<Cache> caches = Sets.newLinkedHashSet();

        for (String name : getCacheNames()) {
            caches.add(new CouchbaseCache(name));
        }
        return caches;
    }
}
