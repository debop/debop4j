package kr.debop4j.core.cache.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * kr.debop4j.core.cache.redis.RedisCacheConfiguration
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 25. 오후 11:50
 */
@Configuration
@EnableCaching
@PropertySource("classpath:redis.properties")
public class RedisCacheConfiguration {

    @Autowired
    Environment env;
}
