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
 * Google Guava 의 {@link com.google.common.cache.LoadingCache} 를 이용하여, 캐시 값을 구하는 방법을 미리 지정하여, 쉽게 캐시를 운영할 수 있도록 캐시입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 12. 5.
 */
@Slf4j
public class FutureWebCacheRepository extends CacheRepositoryBase {

    private final LoadingCache<String, String> cache;

    public FutureWebCacheRepository() {
        if (log.isDebugEnabled())
            log.debug("FutureWebCacheRepository 인스턴스를 생성합니다.");

        cache = CacheBuilder.newBuilder().weakValues().build(getCacheLoader());
    }

    @Override
    public Object get(String key) {
        try {
            return cache.get(key);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void set(String key, Object value, long validFor) {
        String str = (value != null) ? value.toString() : "";
        cache.put(key, str);
    }

    @Override
    public void remove(String key) {
        cache.invalidate(key);
    }

    @Override
    public void removeAll(String... keys) {
        cache.invalidateAll(Arrays.asList(keys));
    }

    @Override
    public void removeAll(Iterable<String> keys) {
        cache.invalidateAll(keys);
    }

    @Override
    public boolean exists(String key) {
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

                if (log.isDebugEnabled())
                    log.debug("URI=[{}] 의 웹 컨텐츠를 비동기 방식으로 다운로드 받아 캐시합니다.", key);

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
                                  key, StringTool.ellipsisChar(responseStr, 255));
                } finally {
                    httpClient.shutdown();
                }
                return responseStr;
            }
        };
    }
}
