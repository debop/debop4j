package kr.debop4j.core.cacherepository;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.spy.memcached.MemcachedClient;

import java.util.Map;
import java.util.concurrent.Future;

import static kr.debop4j.core.Guard.shouldNotBeNull;
import static kr.debop4j.core.Guard.shouldNotBeWhiteSpace;


/**
 * Memcached 를 캐시로 사용하는 Repository입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 12.
 */
@Slf4j
public class MemcachedRepository extends CacheRepositoryBase {

    @Getter
    @Setter
    private MemcachedClient client;

    public MemcachedRepository() {
    }

    public MemcachedRepository(MemcachedClient client) {
        this.client = shouldNotBeNull(client, "client");
    }

    public Future<Object> getAsync(String key) {
        shouldNotBeWhiteSpace(key, "key");
        return client.asyncGet(key);
    }

    /**
     * 캐시에서 해당 키의 항목을 가져옵니다.
     *
     * @param key 캐시 키
     * @return 캐시 항목, 없으면 null 반환
     */
    @Override
    public Object get(String key) {
        shouldNotBeWhiteSpace(key, "key");
        return client.get(key);
    }

    public Map<String, Object> gets(String... keys) {
        return client.getBulk(keys);
    }

    /**
     * 캐시에 항목을 저장합니다.
     *
     * @param key      캐시 키
     * @param value    캐시 항목
     * @param validFor 캐시 유효 기간 (단위 : minutes), 0 이하인 경우는 유효기간이 없다.
     */
    @Override
    public void set(String key, Object value, long validFor) {
        shouldNotBeWhiteSpace(key, "key");
        client.set(key, (int) validFor, value);
    }

    /**
     * 해당 키의 캐시 항목을 삭제합니다.
     *
     * @param key 캐시 키
     */
    @Override
    public void remove(final String key) {
        shouldNotBeWhiteSpace(key, "key");
        client.delete(key);
    }


    /**
     * 해당 키의 캐시 항목의 존재 여부를 알아봅니다.
     *
     * @param key 캐시 키
     * @return 캐시 항목 존재 여부
     */
    @Override
    public boolean exists(String key) {
        shouldNotBeWhiteSpace(key, "key");
        return client.get(key) != null;
    }

    /**
     * 캐시의 모든 항목을 삭제합니다.
     */
    @Override
    public void clear() {
        if (log.isDebugEnabled())
            log.debug("모든 캐시 항목을 제거합니다.");
        client.flush();
    }
}
