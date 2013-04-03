package org.hibernate.cache.redis.impl;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.cache.spi.Region;
import org.hibernate.cache.spi.RegionFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import javax.transaction.TransactionManager;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * org.hibernate.cache.redis.impl.BaseRegion
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 4. 오전 12:43
 */
@Slf4j
public abstract class BaseRegion implements Region {

    private enum InvalidateState {
        INVALID, CLEARING, VALID
    }

    private final String name;
    private final TransactionManager tm;
    private final Object invalidationMutex = new Object();
    private final AtomicReference<InvalidateState> invalidateState =
            new AtomicReference<InvalidateState>(InvalidateState.VALID);
    private final RegionFactory factory;

    protected final RedisTemplate redisTemplate;

    public BaseRegion(RedisTemplate redisTemplate, String name, RegionFactory factory, TransactionManager tm) {
        this.redisTemplate = redisTemplate;
        this.name = name;
        this.tm = tm;
        this.factory = factory;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getElementCountInMemory() {
        return (Long) redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.dbSize();
            }
        });
    }

    public void invalidateRegion() {
        if (log.isTraceEnabled())
            log.trace("Invalidate region:" + name);

        Set keys = redisTemplate.keys(name + ":*");
        redisTemplate.delete(keys);

        if (log.isDebugEnabled())
            log.debug("delete item =[{}]", keys.size());
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }
}
