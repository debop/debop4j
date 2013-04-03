package org.hibernate.cache.redis;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.*;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cfg.Settings;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisShardInfo;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Hibernate 용 2nd Cache using Redis
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 3. 오후 9:58
 */
@Slf4j
public abstract class AbstractRedisRegionFactory implements RegionFactory {

    private static final long serialVersionUID = -2168410627407795095L;

    public static final String REDIS_CONFIGURATION_RESOURCE_NAME = "org.redis.configurationResourceName";

    @Getter
    @Setter
    protected volatile RedisTemplate redisTemplate;

    @Getter
    @Setter
    protected Settings settings;

    @Getter
    private List<String> regionNames = new ArrayList<String>();


    protected AbstractRedisRegionFactory() {}

    protected AbstractRedisRegionFactory(Properties props) {}


    @Override
    public void start(Settings settings, Properties properties) throws CacheException {
        if (log.isInfoEnabled())
            log.info("Starting Redis region factory...");

        try {
            redisTemplate = createRedisTemplate(properties);

        } catch (CacheException ce) {
            throw ce;
        } catch (Throwable t) {
            throw new CacheException("Unable to start Redis region factory", t);
        }
    }

    protected RedisTemplate createRedisTemplate(Properties props) {

        if (log.isInfoEnabled())
            log.info("Create RedisTemplate ...");

        String host = props.getProperty("redis.host", "localhost");
        Integer port = Integer.valueOf(props.getProperty("redis.port", "6379"));
        Integer timeout = Integer.valueOf(props.getProperty("redis.timeout", "2000"));
        Integer database = Integer.valueOf(props.getProperty("redis.database", "0"));
        Boolean usePool = Boolean.valueOf(props.getProperty("redis.usePool", "true"));

        JedisShardInfo shard = new JedisShardInfo(host, port, timeout);
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(shard);
        connectionFactory.setDatabase(database);
        connectionFactory.setUsePool(usePool);

        StringRedisSerializer serializer = new StringRedisSerializer(Charset.forName("UTF-8"));

        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(serializer);
        redisTemplate.setHashKeySerializer(serializer);
        redisTemplate.afterPropertiesSet();

        if (log.isInfoEnabled())
            log.info("Create RedisTemplate is success!");

        return redisTemplate;
    }

    @Override
    public void stop() {
        if (log.isInfoEnabled())
            log.info("Stop Redis region factory.");
        try {
            stopCacheRegions();
            stopCacheManager();
        } catch (Exception e) {
            log.warn("RedisRegionFactory를 중지하는데 실패했습니다.", e);
        }
    }

    protected void stopCacheRegions() {
        if (log.isDebugEnabled())
            log.debug("Clear region references");

        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return null;
            }
        });
    }

    protected void stopCacheManager() {
        if (log.isDebugEnabled())
            log.debug("Stop cache");
        redisTemplate = null;
    }

    @Override
    public boolean isMinimalPutsEnabledByDefault() {
        return true;
    }

    @Override
    public AccessType getDefaultAccessType() {
        return AccessType.TRANSACTIONAL;
    }

    @Override
    public long nextTimestamp() {
        return System.currentTimeMillis() / 100;
    }

    @Override
    public EntityRegion buildEntityRegion(String regionName, Properties properties, CacheDataDescription metadata) throws CacheException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public NaturalIdRegion buildNaturalIdRegion(String regionName, Properties properties, CacheDataDescription metadata) throws CacheException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public CollectionRegion buildCollectionRegion(String regionName, Properties properties, CacheDataDescription metadata) throws CacheException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public QueryResultsRegion buildQueryResultsRegion(String regionName, Properties properties) throws CacheException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public TimestampsRegion buildTimestampsRegion(String regionName, Properties properties) throws CacheException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
