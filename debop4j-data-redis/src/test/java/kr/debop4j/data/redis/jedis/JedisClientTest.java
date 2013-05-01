package kr.debop4j.data.redis.jedis;

import kr.debop4j.core.unitTesting.TestTool;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

/**
 * {@link JedisClient} 테스트
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 1. 오후 3:05
 */
public class JedisClientTest {

    private JedisPool jedisPool;
    private JedisClient client;

    @Before
    public void before() {
        jedisPool = new JedisPool("localhost");
        client = new JedisClient(jedisPool);
    }

    @After
    public void after() {
        client.flushDb();
        jedisPool.destroy();
    }

    @Test
    public void connectionTest() {
        assertThat(client.ping()).isEqualToIgnoringCase("pong");
    }

    @Test
    public void jedisPoolTest() {
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
        client.set("key", 123);
        assertThat(client.get("key")).isEqualTo(123);
    }

    @Test
    public void expireTest() throws Exception {
        client.set("expireTest", "Value", 1);
        assertThat(client.get("expireTest")).isEqualTo("Value");
        Thread.sleep(1000);
        assertThat(client.get("expireTest")).isNull();
    }

    @Test
    public void flushDbTest() throws Exception {
        client.set("a", "a");
        assertThat(client.dbSize()).isGreaterThan(0);
        client.flushDb();
        assertThat(client.dbSize()).isEqualTo(0);
    }

    @Test
    public void deleteTest() throws Exception {
        client.set("d", "d");
        assertThat(client.get("d")).isEqualTo("d");
        assertThat(client.exists("d")).isTrue();

        client.del("d");
        assertThat(client.get("d")).isNull();
        assertThat(client.exists("d")).isFalse();
    }

    @Test
    public void mgetTest() throws Exception {
        int count = 100;
        List<Integer> keys = new ArrayList<Integer>();
        for (int i = 0; i < count; i++) {
            client.set(i, i);
            keys.add(i);
        }
        List<Object> values = client.mget(keys);
        assertThat(values.size()).isEqualTo(count);
    }

    @Test
    public void mdelTest() throws Exception {
        int count = 100;
        List<Integer> keys = new ArrayList<Integer>();
        for (int i = 0; i < count; i++) {
            client.set(i, i);
            keys.add(i);
        }
        List<Object> values = client.mget(keys);
        assertThat(values.size()).isEqualTo(count);

        client.mdel(keys);
        for (int i = 0; i < count; i++) {
            assertThat(client.get(i)).isNull();
        }
    }


    @Test
    public void keysInRegion() throws Exception {
        client.flushDb();

        int count = 100;
        List<Integer> keys = new ArrayList<Integer>();
        for (int i = 0; i < count; i++) {
            client.set(i, i);
            keys.add(i);
        }

        Set<Object> keysInRegion = client.keysInRegion(JedisClient.DEFAULT_REGION_NAME);
        assertThat(keysInRegion.size()).isEqualTo(count);

        client.deleteRegion(JedisClient.DEFAULT_REGION_NAME);
        keysInRegion = client.keysInRegion(JedisClient.DEFAULT_REGION_NAME);
        assertThat(keysInRegion.size()).isEqualTo(0);
    }
}
