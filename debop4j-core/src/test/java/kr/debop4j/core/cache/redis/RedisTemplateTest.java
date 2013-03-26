package kr.debop4j.core.cache.redis;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisShardInfo;

/**
 * kr.debop4j.core.cache.redis.RedisTemplateTest
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 26. 오전 11:20
 */
public class RedisTemplateTest {

    public JedisShardInfo jedisShardInfo() {
        return new JedisShardInfo("localhost");
    }

    public RedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory(jedisShardInfo());
        return factory;
    }

    @Test
    public void redisTemplateTest() {
        StringRedisTemplate template = new StringRedisTemplate(redisConnectionFactory());

        String key = "debop";
        String email = "sunghyouk.bae@gmail.com";

        template.opsForValue().setIfAbsent(key, email);
        String loaded = template.opsForValue().get(key);

        Assert.assertEquals(email, loaded);
    }
}
