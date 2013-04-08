package kr.debop4j.data.redis.client;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import javax.inject.Inject;

/**
 * Redis Client 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 8. 오후 2:10
 */
@Slf4j
public class RedisClient {

    private JedisPool pool;

    @Inject
    public RedisClient(JedisPool pool) {
        assert pool != null;
        this.pool = pool;
    }

    /**
     * Transaction 하에서 지정한 action을 수행합니다. 예외 시에는 모든 작업이 취소됩니다.
     *
     * @param action
     */
    public void withinTx(RedisAction action) {

        Jedis jedis = pool.getResource();
        Transaction tx = jedis.multi();

        try {
            action.run(jedis);
            tx.exec();
        } catch (Exception e) {
            tx.discard();
            tx.discard();
            log.error("Transaction 하에서 작업 시 예외가 발생했습니다.", e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    /**
     * Transaction 하에서 지정한 action을 수행합니다. 예외 시에는 모든 작업이 취소됩니다.
     *
     * @param callback
     * @param <T>
     * @return
     */
    public <T> T withinTx(RedisCallback<T> callback) {

        Jedis jedis = pool.getResource();
        Transaction tx = jedis.multi();

        try {
            T result = callback.execute(jedis);
            tx.exec();
            return result;
        } catch (Exception e) {
            tx.discard();
            log.error("Transaction 하에서 작업 시 예외가 발생했습니다.", e);
        } finally {
            pool.returnResource(jedis);
        }
        return null;
    }
}
