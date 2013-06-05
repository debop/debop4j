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

import java.util.Map;
import java.util.concurrent.Future;

/**
 * 비동기 방식으로 캐시 작업을 수행합니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 12.
 */
public interface IAsyncCacheRepository {

    /**
     * Gets async.
     *
     * @param key the key
     * @return the async
     */
    Future<Object> getAsync(final String key);

    /**
     * Gets async.
     *
     * @param key the key
     * @return the async
     */
    Future<Map> getsAsync(final String... key);

    /**
     * Sets async.
     *
     * @param key      the key
     * @param value    the value
     * @param validFor the valid for
     * @return the async
     */
    Future<Void> setAsync(final String key, Object value, long validFor);

    /**
     * Remove async.
     *
     * @param key the key
     * @return the future
     */
    Future<Void> removeAsync(final String key);

    /**
     * Remove all async.
     *
     * @param keys the keys
     * @return the future
     */
    Future<Void> removeAllAsync(final String... keys);

    /**
     * Remove all async.
     *
     * @param keys the keys
     * @return the future
     */
    Future<Void> removeAllAsync(final Iterable<String> keys);

    /**
     * Clear async.
     *
     * @return the future
     */
    Future<Void> clearAsync();
}
