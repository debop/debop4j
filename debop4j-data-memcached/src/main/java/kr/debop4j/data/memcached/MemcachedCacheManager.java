package kr.debop4j.data.memcached;

import com.google.common.collect.Sets;
import kr.debop4j.core.Guard;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.spy.memcached.MemcachedClient;
import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;

import java.util.Collection;

/**
 * kr.debop4j.core.cache.memcached.MemcachedCacheManager
 *
 * @author sunghyouk.bae@gmail.com
 *         Date: 13. 3. 25 오전 11:42
 */
@Slf4j
public class MemcachedCacheManager extends AbstractTransactionSupportingCacheManager {

    @Getter
    private MemcachedCache memcachedCache;

    public MemcachedCacheManager(MemcachedClient memcachedClient) {
        this(memcachedClient, 24 * 60 * 60);
    }

    public MemcachedCacheManager(MemcachedClient memcachedClient, int expireSeconds) {
        Guard.shouldNotBeNull(memcachedClient, "memcachedClient");
        memcachedCache = new MemcachedCache(memcachedClient, expireSeconds);
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        super.getCacheNames();
        return Sets.newHashSet(memcachedCache);
    }

    @Override
    public Cache getCache(String name) {
        return memcachedCache;
    }
}
