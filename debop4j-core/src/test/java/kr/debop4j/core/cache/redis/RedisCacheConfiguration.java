package kr.debop4j.core.cache.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisShardInfo;

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
    public RedisTemplate redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }

    @Bean
    public RedisCacheManager redisCacheManager() {
        return new RedisCacheManager(redisTemplate(), 300);
    }
}
