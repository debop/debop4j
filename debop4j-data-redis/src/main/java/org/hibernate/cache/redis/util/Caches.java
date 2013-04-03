package org.hibernate.cache.redis.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * org.hibernate.cache.redis.util.Caches
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 3. 오후 10:33
 */
@Slf4j
@SuppressWarnings("unchecked")
public class Caches {

    private Caches() {}

    public static <T> T execute(RedisTemplate redis, RedisCallback<T> action) {
        return (T) redis.execute(action);
    }
}
