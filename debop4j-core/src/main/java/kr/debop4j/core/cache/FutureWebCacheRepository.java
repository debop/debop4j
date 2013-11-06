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

import com.google.common.base.Charsets;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import kr.debop4j.core.tools.StringTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.DefaultHttpAsyncClient;
import org.apache.http.nio.client.HttpAsyncClient;
import org.apache.http.util.EntityUtils;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Google Guava 의 {@link com.google.common.cache.LoadingCache} 를 이용하여,
 * 캐시 값을 구하는 방법을 미리 지정하여, 쉽게 캐시를 운영할 수 있도록 캐시입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 5.
 */
@Slf4j
public class FutureWebCacheRepository extends CacheRepositoryBase {

    private final LoadingCache<String, String> cache;

    /** Instantiates a new Future web cache repository. */
    public FutureWebCacheRepository() {
        cache = CacheBuilder.newBuilder().weakValues().build(getCacheLoader());
    }

    @Override
    public Object get(final String key) throws ExecutionException {
        return cache.get(key);
    }

    @Override
    public void set(final String key, final Object value, long validFor) {
        String str = (value != null) ? value.toString() : "";
        cache.put(key, str);
    }

    @Override
    public void remove(final String key) {
        cache.invalidate(key);
    }

    @Override
    public void removeAll(final String... keys) {
        cache.invalidateAll(Arrays.asList(keys));
    }

    @Override
    public void removeAll(final Iterable<String> keys) {
        cache.invalidateAll(keys);
    }

    @Override
    public boolean exists(final String key) {
        return cache.getIfPresent(key) != null;
    }

    @Override
    public void clear() {
        cache.cleanUp();
    }

    private static synchronized CacheLoader<String, String> getCacheLoader() {
        return new CacheLoader<String, String>() {
            @Override
            public String load(String key) throws Exception {


                    log.trace("URI=[{}] 의 웹 컨텐츠를 비동기 방식으로 다운로드 받아 캐시합니다.", key);

                String responseStr = "";
                HttpAsyncClient httpClient = new DefaultHttpAsyncClient();
                try {
                    httpClient.start();
                    HttpGet request = new HttpGet(key);
                    Future<HttpResponse> future = httpClient.execute(request, null);

                    HttpResponse response = future.get();
                    responseStr = EntityUtils.toString(response.getEntity(), Charsets.UTF_8.toString());

                    if (log.isDebugEnabled())
                        log.debug("URI=[{}]로부터 웹 컨텐츠를 다운로드 받았습니다. responseStr=[{}]",
                                key, StringTool.ellipsisChar(responseStr, 80));
                } finally {
                    httpClient.shutdown();
                }
                return responseStr;
            }
        };
    }
}
