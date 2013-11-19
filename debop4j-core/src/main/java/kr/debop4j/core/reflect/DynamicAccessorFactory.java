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

package kr.debop4j.core.reflect;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

/**
 * {@link DynamicAccessor} 의 생성자입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 1. 21
 */
@Slf4j
public class DynamicAccessorFactory {

    private static final CacheLoader<Class<?>, DynamicAccessor> loader;
    private static final LoadingCache<Class<?>, DynamicAccessor> cache;

    static {
        log.info("DynamicAccessor 캐시를 생성합니다.");

        loader = new CacheLoader<Class<?>, DynamicAccessor>() {
            @Override
            @SuppressWarnings("unchecked")
            public DynamicAccessor<?> load(Class<?> type) throws Exception {
                return new DynamicAccessor(type);
            }
        };

        cache = CacheBuilder.newBuilder().weakValues().maximumSize(2000).build(loader);

        log.info("DynamicAccessor 캐시를 생성했습니다.");
    }

    /**
     * Create dynamic accessor.
     *
     * @param targetType the target type
     * @return the dynamic accessor
     */
    @SuppressWarnings("unchecked")
    public static <T> DynamicAccessor<T> create(Class<T> targetType) {
        try {
            return (DynamicAccessor<T>) cache.get(targetType);
        } catch (ExecutionException e) {
            log.error("DynamicAccessor 를 생성하는데 실패했습니다. targetType=" + targetType.getName(), e);
            return null;
        }
    }

    /**
     * Clear void.
     */
    public static synchronized void clear() {
        cache.cleanUp();
    }
}
