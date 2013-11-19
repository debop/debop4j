/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.debop4j.core.cache;

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

import static kr.debop4j.core.Guard.shouldNotBeNull;
import static kr.debop4j.core.Guard.shouldNotBeWhiteSpace;


/**
 * Hash Map 을 캐시 저장소로 사용합니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 12
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

    public ImmutableMap getAllPresent(final Iterable<?> keys) {
        return cache.getAllPresent(keys);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(final String key, final Object value, long validFor) {
        shouldNotBeWhiteSpace(key, "key");
        cache.put(key, value);
    }

    public void setAll(final Map m) {
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
    public void removeAll(final String... keys) {
        cache.invalidateAll(Arrays.asList(keys));
    }

    @Override
    public void removeAll(final Iterable<String> keys) {
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
