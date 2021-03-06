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
import kr.debop4j.core.parallelism.AsyncTool;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static kr.debop4j.core.Guard.shouldNotBeWhiteSpace;


/**
 * {@link java.util.concurrent.ConcurrentHashMap}을 캐시 저장소로 사용하는 비동기 캐시 저장소입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 12
 */
@Slf4j
@SuppressWarnings("unchecked")
public class ConcurrentHashMapCacheRepository extends CacheRepositoryBase {

    private final Cache<String, Object> cache;

    /**
     * Instantiates a new Concurrent hash map cache repository.
     *
     * @param validFor the valid for
     */
    public ConcurrentHashMapCacheRepository(long validFor) {
        if (validFor > 0)
            setExpiry(validFor);

        CacheBuilder builder = CacheBuilder.newBuilder().concurrencyLevel(4);

        if (validFor > 0)
            builder.expireAfterAccess(validFor, TimeUnit.MINUTES);

        cache = builder.build();
    }

    /**
     * 캐시 저장소
     *
     * @return 캐시 저장소를 반환합니다.
     */
    public Map<String, Object> getCache() {
        return this.cache.asMap();
    }

    /**
     * 캐시에서 해당 키의 항목을 가져옵니다.
     *
     * @param key 캐시 키
     * @return 캐시 항목, 없으면 null 반환
     */
    @Override
    public Object get(final String key) {
        shouldNotBeWhiteSpace(key, "key");
        return cache.getIfPresent(key);
    }

    /**
     * Gets async.
     *
     * @param key the key
     * @return the async
     */
    public Future<Object> getAsync(final String key) {
        return AsyncTool.startNew(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return cache.getIfPresent(key);
            }
        });
    }

    /**
     * 캐시에 항목을 저장합니다.
     *
     * @param key      캐시 키
     * @param value    캐시 항목
     * @param validFor 캐시 유효 기간 (단위 : minutes), 0 이하인 경우는 유효기간이 없다.
     */
    @Override
    public void set(final String key, final Object value, final long validFor) {
        shouldNotBeWhiteSpace(key, "key");
        cache.put(key, value);
    }

    /**
     * 해당 키의 캐시 항목을 삭제합니다.
     *
     * @param key 캐시 키
     */
    @Override
    public void remove(final String key) {
        shouldNotBeWhiteSpace(key, "key");
        cache.invalidate(key);
    }

    /**
     * 여러개의 키를 모두 삭제합니다.
     *
     * @param keys 캐시 키 시퀀스
     */
    @Override
    public void removeAll(final String... keys) {
        cache.invalidateAll(Arrays.asList(keys));
    }

    /**
     * 여러개의 키를 모두 삭제합니다.
     *
     * @param keys 캐시 키 시퀀스
     */
    @Override
    public void removeAll(final Iterable<String> keys) {
        cache.invalidateAll(keys);
    }

    /**
     * 해당 키의 캐시 항목의 존재 여부를 알아봅니다.
     *
     * @param key 캐시 키
     * @return 캐시 항목 존재 여부
     */
    @Override
    public boolean exists(final String key) {
        shouldNotBeWhiteSpace(key, "key");
        return cache.getIfPresent(key) != null;
    }

    /** 캐시의 모든 항목을 삭제합니다. */
    @Override
    public synchronized void clear() {
        cache.cleanUp();
    }
}
