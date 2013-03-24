package kr.debop4j.core.cache;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;

import java.util.Collection;

/**
 * Couchbase를 캐시 저장소로 사용하는 Spring Cache 용 CacheManager 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 24. 오후 6:00
 */
@Slf4j
public class CouchbaseCacheManager extends AbstractCacheManager {

    @Getter
    @Setter
    private String[] cacheNames = new String[0];

    @Override
    protected Collection<? extends Cache> loadCaches() {
        Collection<Cache> caches = Sets.newLinkedHashSetWithExpectedSize(cacheNames.length);

        for (String name : cacheNames) {
            caches.add(new CouchbaseCache(name));
        }
        return caches;
    }
}
