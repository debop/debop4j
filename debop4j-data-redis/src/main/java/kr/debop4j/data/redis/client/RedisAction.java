package kr.debop4j.data.redis.client;

import redis.clients.jedis.Jedis;

/**
 * kr.debop4j.data.redis.client.RedisAction
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 8. 오후 2:30
 */
public interface RedisAction {

    /**
     * 실행할 함수
     */
    void run(Jedis jedis);
}
