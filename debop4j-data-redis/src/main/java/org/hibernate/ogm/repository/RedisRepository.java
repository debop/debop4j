package org.hibernate.ogm.repository;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.ogm.grid.EntityKey;
import org.hibernate.redis.jedis.JedisCallback;
import org.hibernate.redis.jedis.JedisTransactionalCallback;
import org.hibernate.redis.serializer.BinaryRedisSerializer;
import org.hibernate.redis.serializer.RedisSerializer;
import org.hibernate.redis.serializer.SerializationTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.util.*;

/**
 * hibernate-ogm 의 엔티티에 대한 CRUD를 담당하는 Repository입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 3. 오후 11:57
 */
@SuppressWarnings("unchecked")
public class RedisRepository {

    private static final Logger log = LoggerFactory.getLogger(RedisRepository.class);
    private static final boolean isTraceEnabled = log.isTraceEnabled();
    private static final boolean isDebugEnabled = log.isDebugEnabled();

    @Getter
    private final JedisPool jedisPool;

    @Getter
    @Setter
    private int database;

    @Getter
    private static final RedisSerializer fieldSerializer = new BinaryRedisSerializer<Object>();

    @Getter
    private static final RedisSerializer valueSerializer = new BinaryRedisSerializer<Object>();

    public static final String ENTITY_HSET = "OGM-Entity";
    public static final byte[] RAW_ENTITY_HSET = fieldSerializer.serialize(ENTITY_HSET);

    public static final String ASSOCIATION_HSET = "OGM_Association";
    public static final byte[] RAW_ASSOCIATION_HSET = fieldSerializer.serialize(ASSOCIATION_HSET);

    public static final String SEQUENCE_HSET = "OGM-Sequence";
    public static final byte[] RAW_SEQUENCE_HSET = fieldSerializer.serialize(SEQUENCE_HSET);


    public RedisRepository(JedisPool pool) {
        this(pool, 0);
    }

    public RedisRepository(JedisPool pool, int database) {
        this.jedisPool = pool;
        this.database = database;
    }


    public Object getEntitySet(EntityKey key) {

        log.trace("엔티티 집합을 조회합니다... key=[{}]", key);

        final byte[] rawField = fieldSerializer.serialize(key);

        byte[] rawValue = run(new JedisCallback<byte[]>() {
            @Override
            public byte[] execute(Jedis jedis) {
                return jedis.hget(RAW_ENTITY_HSET, rawField);
            }
        });
        return valueSerializer.deserialize(rawValue);
    }

    public void putEntity(EntityKey key, Map<String, Object> tuple) {


        log.trace("엔티티를 저장합니다. key=[{}], tuple=[{}]", key, tuple);

        final byte[] rawField = fieldSerializer.serialize(key);
        final byte[] rawValue = valueSerializer.serialize(tuple);
        try {
            run(new JedisCallback<Long>() {
                @Override
                public Long execute(Jedis jedis) {
                    return jedis.hset(RAW_ENTITY_HSET, rawField, rawValue);
                }
            });
        } catch (Exception e) {
            log.error("엔티티 저장에 실패했습니다.", e);
            throw new RuntimeException(e);
        }
    }

    public void removeEntity(EntityKey key) {

        log.trace("엔티티를 삭제합니다... key=[{}]", key);
        final byte[] rawField = fieldSerializer.serialize(key);
        Long result = run(new JedisCallback<Long>() {
            @Override
            public Long execute(Jedis jedis) {
                return jedis.hdel(RAW_ENTITY_HSET, rawField);
            }
        });
    }

    public byte[] rawField(EntityKey key) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("columNames", Integer.toString(key.hashCode()));
        map.put("table", key.getTable());
        return fieldSerializer.serialize(map);
    }

    /**
     * 키를 byte[] 로 직렬화합니다 *
     */
    @SuppressWarnings("unchecked")
    private byte[] rawKey(Object key) {
        return getFieldSerializer().serialize(key);
    }

    @SuppressWarnings("unchecked")
    private byte[][] rawKeys(Collection<? extends Object> keys) {
        byte[][] rawKeys = new byte[keys.size()][];
        int i = 0;
        for (Object key : keys) {
            rawKeys[i++] = getFieldSerializer().serialize(key);
        }
        return rawKeys;
    }

    /**
     * byte[] 를 key 값으로 역직렬화 합니다
     */
    private Object deserializeKey(byte[] rawKey) {
        return getFieldSerializer().deserialize(rawKey);
    }

    /**
     * 캐시 값을 byte[]로 직렬화를 수행합니다.
     */
    @SuppressWarnings("unchecked")
    private byte[] rawValue(Object value) {
        return getValueSerializer().serialize(value);
    }

    /**
     * byte[] 를 역직렬화하여 원 객체로 변환합니다.
     */
    private Object deserializeValue(byte[] rawValue) {
        return getValueSerializer().deserialize(rawValue);
    }

    /**
     * Redis 작업을 수행합니다.<br/>
     * {@link redis.clients.jedis.JedisPool} 을 이용하여, {@link redis.clients.jedis.Jedis}를 풀링하여 사용하도록 합니다.
     */
    private <T> T run(final JedisCallback<T> callback) {

        final Jedis jedis = jedisPool.getResource();

        try {
            if (database != 0) jedis.select(database);
            return callback.execute(jedis);
        } catch (Throwable t) {
            log.error("Redis 작업 중 예외가 발생했습니다.", t);
            throw new RuntimeException(t);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 복수의 작업을 하나의 Transaction 하에서 수행하도록 합니다.<br />
     * {@link redis.clients.jedis.JedisPool} 을 이용하여, {@link redis.clients.jedis.Jedis}를 풀링하여 사용하도록 합니다.
     */
    private List<Object> runWithTx(final JedisTransactionalCallback callback) {

        final Jedis jedis = jedisPool.getResource();

        try {
            if (database != 0) jedis.select(database);
            Transaction tx = jedis.multi();
            callback.execute(tx);
            return tx.exec();
        } catch (Throwable t) {
            log.error("Redis 작업 중 예외가 발생했습니다.", t);
            throw new RuntimeException(t);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * Raw Key 값들을 역직렬화하여 Key Set을 반환합니다.
     */
    @SuppressWarnings("unchecked")
    private Set<Object> deserializeKeys(Set<byte[]> rawKeys) {
        return SerializationTool.deserialize(rawKeys, getFieldSerializer());
    }

    /**
     * Raw Value 값들을 역직렬화하여 Value List를 반환합니다.
     */
    @SuppressWarnings("unchecked")
    private List<Object> deserializeValues(List<byte[]> rawValues) {
        return SerializationTool.deserialize(rawValues, getValueSerializer());
    }
}
