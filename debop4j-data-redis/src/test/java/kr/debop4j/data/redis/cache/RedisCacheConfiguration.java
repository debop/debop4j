/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.debop4j.data.redis.cache;

import lombok.extern.slf4j.Slf4j;
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
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 *         13. 3. 25. 오후 11:50
 */
@Configuration
@EnableCaching
@ComponentScan(basePackageClasses = { UserRepository.class })
@PropertySource(name = "redis", value = { "classpath:/redis.properties" })
@Slf4j
public class RedisCacheConfiguration {

    @Autowired
    Environment env;

    private @Value("${redis.host}") String redisHostName;
    private @Value("${redis.port}") int redisPort;
    private @Value("${redis.usePool}") boolean redisUsePool;

    /** @PropertySource, @Value를 사용하려면 PropertySourcesPlaceholderConfigurer를 정의해줘야 한다. */
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public JedisShardInfo jedisShardInfo() {
        JedisShardInfo shardInfo = new JedisShardInfo(redisHostName, redisPort);
        log.info("create JedisShardInfo=[{}]", shardInfo);
        return shardInfo;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory(jedisShardInfo());
        factory.setUsePool(redisUsePool);
        log.info("create RedisConnectionFactory!!!");
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
        log.info("create RedisTemplate!!!");
        return template;
    }

    @Bean
    public RedisCacheManager redisCacheManager() {
        log.info("create RedisCacheManager...");
        return new RedisCacheManager(redisTemplate());
    }
}
