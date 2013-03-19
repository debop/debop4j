package kr.debop4j.core.cache;

import java.util.Map;
import java.util.concurrent.Future;

/**
 * 비동기 방식으로 캐시 작업을 수행합니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 12.
 */
public interface IAsyncCacheRepository {

    Future<Object> getAsync(final String key);

    Future<Map> getsAsync(String... key);

    Future<Void> setAsync(final String key, Object value, long validFor);

    Future<Void> removeAsync(final String key);

    Future<Void> removeAllAsync(String... keys);

    Future<Void> removeAllAsync(Iterable<String>... keys);

    Future<Void> clearAsync();
}
