package kr.debop4j.data.redis.cache;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Redis 를 저장소로 사용하는 Cache
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 26. 오전 11:43
 */
@Slf4j
public class RedisCache implements Cache {
    @Getter
    private String name;
    @Getter
    private int expireSeconds;

    private RedisTemplate redisTemplate;

    public RedisCache(String name, RedisTemplate redisTemplate) {
        this(name, redisTemplate, 300);
    }

    public RedisCache(String name, RedisTemplate redisTemplate, int expireSeconds) {
        assert redisTemplate != null;

        this.name = name;
        this.redisTemplate = redisTemplate;

        if (log.isDebugEnabled())
            log.debug("RedisCache를 생성합니다. name=[{}], redisTemplate=[{}]", name, redisTemplate);
    }

    @Override
    public Object getNativeCache() {
        return redisTemplate;
    }

    public String getKey(Object key) {
        return name + ":" + key;
    }

    @Override
    public ValueWrapper get(Object key) {
        assert key != null;

        if (log.isDebugEnabled())
            log.debug("캐시 키[{}] 값을 구합니다...", key);

        Object result = redisTemplate.opsForValue().get(getKey(key));

        SimpleValueWrapper wrapper = null;
        if (result != null) {
            if (log.isDebugEnabled())
                log.debug("캐시 값을 로드했습니다. key=[{}]", key);
            wrapper = new SimpleValueWrapper(result);
        }
        return wrapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void put(Object key, Object value) {
        assert key != null;

        if (log.isDebugEnabled())
            log.debug("캐시에 값을 저장합니다. key=[{}], value=[{}]", key, value);

        redisTemplate.opsForValue().set(getKey(key), value);
        if (expireSeconds > 0)
            redisTemplate.expire(getKey(key), expireSeconds, TimeUnit.SECONDS);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void evict(Object key) {
        assert key != null;
        if (log.isDebugEnabled())
            log.debug("지정한 키[{}]의 캐시를 삭제합니다...", key);

        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("캐시 항목 삭제에 실패했습니다. key=" + key, e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        if (log.isDebugEnabled())
            log.debug("모든 캐시를 삭제합니다...");
        try {
            // redisTemplate.multi();
            redisTemplate.execute(new RedisCallback() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    connection.flushAll();
                    return null;
                }
            });
        } catch (Exception e) {
            log.warn("모든 캐시를 삭제하는데 실패했습니다.", e);
        }
    }
}
