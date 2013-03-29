package kr.debop4j.data.redis.cache;

import org.junit.Assert;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * Redis Example
 * 참고: http://krams915.blogspot.kr/2012/02/spring-mvc-31-implement-crud-with_1921.html
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 25. 오후 10:53
 */
public class JedisExampleTest {

    @Test
    public void usingJedis() {
        Jedis jedis = new Jedis("localhost", 6379);
        jedis.connect();
        Assert.assertTrue(jedis.isConnected());

        String key = "debop";
        String email = "sunghyouk.bae@gmail.com";

        if (jedis.exists(key))
            Assert.assertEquals(1L, (long) jedis.del(key));

        jedis.setex(key, 60, email);
        Assert.assertTrue(jedis.exists(key));

        Assert.assertEquals(email, jedis.get(key));

        jedis.disconnect();
    }
}
