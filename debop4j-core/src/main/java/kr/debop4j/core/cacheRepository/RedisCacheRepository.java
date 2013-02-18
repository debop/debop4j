package kr.debop4j.core.cacheRepository;

import com.google.common.collect.Iterables;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.util.List;

import static java.lang.String.format;
import static kr.debop4j.core.Guard.shouldNotBeNull;
import static kr.debop4j.core.Guard.shouldNotBeWhiteSpace;

/**
 * Redis를 캐시로 사용하는 Repository 입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 12
 */
@Slf4j
public class RedisCacheRepository extends CacheRepositoryBase {

    @Setter
    private Jedis jedis;

    public RedisCacheRepository() {
    }

    public RedisCacheRepository(Jedis jedis) {
        this.jedis = shouldNotBeNull(jedis, "jedis");
    }

    public synchronized Jedis getJedis() {
        if (!jedis.isConnected()) {
            if (log.isDebugEnabled())
                log.debug("Redis 서버에 연결합니다...");

            jedis.connect();

            if (log.isDebugEnabled())
                log.debug("Redis 서버에 연결했습니다. redis=" + jedis.info());
        }
        return this.jedis;
    }

    /**
     * 캐시에서 해당 키의 항목을 가져옵니다.
     *
     * @param key 캐시 키
     * @return 캐시 항목, 없으면 null 반환
     */
    @Override
    public Object get(final String key) {
        if (log.isDebugEnabled())
            log.debug("캐시 항목을 읽어 옵니다. key=" + key);

        shouldNotBeWhiteSpace(key, "key");

        return getJedis().get(key);
    }

    public List<String> gets(String... keys) {
        return getJedis().mget(keys);
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
        if (log.isDebugEnabled())
            log.debug(format("캐시 항목을 저장합니다. key=[%s], value=[%s], validFor=[%d] min",
                             key, value, validFor));

        shouldNotBeWhiteSpace(key, "key");

        getJedis().set(key, (value != null) ? value.toString() : "");

        long expirySec = (validFor > 0) ? validFor : getExpiry();

        if (expirySec > 0) {
            if (log.isDebugEnabled())
                log.debug("캐시 항목에 유효 시간을 설정했습니다. expiry (min) = " + expirySec);

            getJedis().expire(key, (int) (expirySec * 60));
        }
    }

    /**
     * 해당 키의 캐시 항목을 삭제합니다.
     *
     * @param key 캐시 키
     */
    @Override
    public void remove(final String key) {
        shouldNotBeWhiteSpace(key, "key");
        getJedis().del(key);
    }

    /**
     * 여러개의 키를 모두 삭제합니다.
     *
     * @param keys 캐시 키 시퀀스
     */
    @Override
    public void removeAll(String... keys) {
        if (keys != null)
            getJedis().del(keys);
    }

    /**
     * 여러개의 키를 모두 삭제합니다.
     *
     * @param keys 캐시 키 시퀀스
     */
    @Override
    public void removeAll(Iterable<String> keys) {
        if (keys != null)
            getJedis().del(Iterables.toArray(keys, String.class));
    }

    /**
     * 해당 키의 캐시 항목의 존재 여부를 알아봅니다.
     *
     * @param key 캐시 키
     * @return 캐시 항목 존재 여부
     */
    public boolean exists(final String key) {
        shouldNotBeWhiteSpace(key, "key");
        return getJedis().exists(key);
    }

    /**
     * 캐시의 모든 항목을 삭제합니다.
     */
    @Override
    public void clear() {
        if (log.isDebugEnabled())
            log.debug("모든 캐시 항목을 삭제합니다...");
        getJedis().flushAll();
    }
}
