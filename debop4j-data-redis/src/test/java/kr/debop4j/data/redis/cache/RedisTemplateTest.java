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

import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisShardInfo;

/**
 * kr.debop4j.core.cache.redis.RedisTemplateTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 *         13. 3. 26. 오전 11:20
 */
public class RedisTemplateTest {

    public JedisShardInfo jedisShardInfo() {
        return new JedisShardInfo("localhost");
    }

    public RedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory(jedisShardInfo());
        factory.setUsePool(true);
        return factory;
    }

    @Test
    public void redisTemplateTest() {
        StringRedisTemplate template = new StringRedisTemplate(redisConnectionFactory());

        String key = "debop-template";
        String email = "sunghyouk.bae@gmail.com";

        template.opsForValue().setIfAbsent(key, email);
        String loaded = template.opsForValue().get(key);

        Assert.assertEquals(email, loaded);
    }
}
