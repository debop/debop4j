package kr.debop4j.data.redis.jedis;

import kr.debop4j.core.unitTesting.TestTool;
import org.junit.Test;
import redis.clients.jedis.JedisPool;

import static org.fest.assertions.Assertions.assertThat;

/**
 * {@link JedisClient} 테스트
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 1. 오후 3:05
 */
public class JedisClientTest {

    @Test
    public void connectionTest() {
        JedisPool jedisPool = new JedisPool("localhost");
        JedisClient client = new JedisClient(jedisPool);

        assertThat(client.ping()).isEqualToIgnoringCase("pong");
    }

    @Test
    public void jedisPoolTest() {
        final JedisPool jedisPool = new JedisPool("localhost");
        final JedisClient client = new JedisClient(jedisPool);

        TestTool.runTasks(1000, new Runnable() {
            @Override
            public void run() {
                client.ping();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignored) {}
            }
        });
    }

    @Test
    public void getAndSet() throws Exception {
        final JedisPool jedisPool = new JedisPool("localhost");
        final JedisClient client = new JedisClient(jedisPool);

        client.set("key", 123);
        assertThat(client.get("key")).isEqualTo(123);
    }
}
