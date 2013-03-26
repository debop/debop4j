package kr.debop4j.redis.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisShardInfo;

import java.nio.charset.Charset;

/**
 * kr.debop4j.core.cache.redis.RedisCacheConfiguration
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 25. 오후 11:50
 */
@Configuration
@EnableCaching
@ComponentScan(basePackageClasses = UserRepository.class)
// @PropertySource("classpath:redis.properties")
public class RedisCacheConfiguration {

    @Autowired
    Environment env;

    @Bean
    public JedisShardInfo jedisShardInfo() {
        return new JedisShardInfo("localhost");
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new JedisConnectionFactory(jedisShardInfo());
    }

    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer(Charset.forName("UTF-8"));
    }

    @Bean
    public RedisTemplate redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(stringRedisSerializer());
        template.setHashKeySerializer(stringRedisSerializer());
        return template;
    }

    @Bean
    public RedisCacheManager redisCacheManager() {
        return new RedisCacheManager(redisTemplate(), 300);
    }
}
