package kr.debop4j.data.redis.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheManager;
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
@PropertySource("classpath:/redis.properties")
public class RedisCacheConfiguration {

    @Autowired
    Environment env;

    @Value("${redis.host}")
    private String redisHostName;
    @Value("${redis.port}")
    private int redisPort;
    @Value("${redis.usePool}")
    private boolean redisUsePool;

    // @PropertySource, @Value를 사용하려면 PropertySourcesPlaceholderConfigurer를 정의해줘야 한다.
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public JedisShardInfo jedisShardInfo() {
        JedisShardInfo shardInfo = new JedisShardInfo(redisHostName, redisPort);
        return shardInfo;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory(jedisShardInfo());
        factory.setUsePool(redisUsePool);
        return factory;
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
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisCacheManager redisCacheManager() {
        return new RedisCacheManager(redisTemplate());
    }
}
