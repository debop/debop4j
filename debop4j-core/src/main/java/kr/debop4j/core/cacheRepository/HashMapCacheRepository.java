package kr.debop4j.core.cacheRepository;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static kr.nsoft.commons.Guard.shouldNotBeNull;
import static kr.nsoft.commons.Guard.shouldNotBeWhiteSpace;


/**
 * 설명을 추가하세요.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 12
 */
@Slf4j
@SuppressWarnings("unchecked")
public class HashMapCacheRepository extends CacheRepositoryBase {

    private final Cache cache;

    public HashMapCacheRepository(Cache<String, Object> cache) {
        this.cache = shouldNotBeNull(cache, "cache");
    }

    public HashMapCacheRepository(long validFor) {
        if (validFor > 0)
            setExpiry(validFor);

        CacheBuilder builder = CacheBuilder.newBuilder();

        if (validFor > 0)
            builder.expireAfterAccess(validFor, TimeUnit.MINUTES);

        cache = builder.build();
    }

    /**
     * {@inheritDoc}
     */
    public ConcurrentMap getCache() {
        return this.cache.asMap();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String key) {
        shouldNotBeWhiteSpace(key, "key");
        return cache.getIfPresent(key);
    }

    public Object get(final String key, Callable<?> valueLoader) throws ExecutionException {
        shouldNotBeWhiteSpace(key, "key");
        return cache.get(key, valueLoader);
    }

    public ImmutableMap getAllPresent(Iterable<?> keys) {
        return cache.getAllPresent(keys);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(final String key, final Object value, final long validFor) {
        shouldNotBeWhiteSpace(key, "key");
        cache.put(key, value);
    }

    public void setAll(Map m) {
        cache.putAll(m);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(final String key) {
        shouldNotBeWhiteSpace(key, "key");
        cache.invalidate(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAll(String... keys) {
        cache.invalidateAll(Arrays.asList(keys));
    }

    @Override
    public void removeAll(Iterable<String> keys) {
        cache.invalidateAll(keys);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists(final String key) {
        shouldNotBeWhiteSpace(key, "key");
        return cache.getIfPresent(key) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        cache.invalidateAll();
    }
}
