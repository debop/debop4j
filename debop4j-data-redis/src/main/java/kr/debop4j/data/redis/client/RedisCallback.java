package kr.debop4j.data.redis.client;

import redis.clients.jedis.Jedis;

/**
 * kr.debop4j.data.redis.client.RedisCallback
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 8. 오후 2:36
 */
public interface RedisCallback<T> {

    T execute(Jedis jedis);
}
