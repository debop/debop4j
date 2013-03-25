package kr.debop4j.core.cache.redis;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis Example
 * 참고: http://krams915.blogspot.kr/2012/02/spring-mvc-31-implement-crud-with_1921.html
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 25. 오후 10:53
 */
public class RedisTemplateTest {

    public RedisConnectionFactory redisConnectionFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxActive(100);
        poolConfig.setMinIdle(2);
        JedisConnectionFactory factory = new JedisConnectionFactory(poolConfig);
        factory.setHostName("127.0.0.1");
        //factory.setPort(6379);
        //factory.setPassword("");
        return factory;
    }

    @Test
    public void connectByJedis() {
        Jedis jedis = new Jedis("localhost", 6379);
        jedis.connect();
        Assert.assertTrue(jedis.isConnected());
        jedis.disconnect();
    }

    @Test
    public void connectionTest() {
        StringRedisTemplate template = new StringRedisTemplate(redisConnectionFactory());
        Assert.assertNotNull(template);

        Assert.assertNotNull(template.opsForSet());
    }

    @Test
    public void addUsers() {

        StringRedisTemplate template = new StringRedisTemplate(redisConnectionFactory());
        //template.setHashKeySerializer(new JacksonJsonRedisSerializer<String>(String.class));
        Assert.assertNotNull(template);

        String email = "sunghyouk.bae@gmail.com";
        template.opsForSet().add("debop", email);
        String loaded = (String) template.opsForSet().pop("debop");

        Assert.assertEquals(email, loaded);
    }
}
