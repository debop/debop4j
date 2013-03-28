package kr.debop4j.nosql.redis.cache;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;

/**
 * Redis를 저장소로 사용하는 Cache Manager 입니다.
 * TODO: Spring 에도 있다. 비교해서 좋은 걸 사용하자.
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 26. 오전 11:43
 */
@Slf4j
@Getter
@Setter
public class RedisCacheManager extends AbstractTransactionSupportingCacheManager {

    private RedisTemplate redisTemplate;
    private int expireSeconds;

    public RedisCacheManager(RedisTemplate redisTemplate) {
        this(redisTemplate, 300);
    }

    public RedisCacheManager(RedisTemplate redisTemplate, int expireSeconds) {
        assert redisTemplate != null;
        this.redisTemplate = redisTemplate;
        this.expireSeconds = expireSeconds;
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        Collection<Cache> caches = Lists.newArrayList();

        for (String name : getCacheNames()) {
            caches.add(new RedisCache(name, redisTemplate, expireSeconds));
        }
        return caches;
    }

    @Override
    public Cache getCache(String name) {
        synchronized (this) {
            Cache cache = super.getCache(name);
            if (cache == null) {
                cache = new RedisCache(name, redisTemplate, expireSeconds);
                addCache(cache);
            }
            return cache;
        }
    }
}
