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
import redis.clients.jedis.Jedis;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Redis Example
 * 참고: http://krams915.blogspot.kr/2012/02/spring-mvc-31-implement-crud-with_1921.html
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 *         13. 3. 25. 오후 10:53
 */
public class JedisExampleTest {

    @Test
    public void usingJedis() {
        Jedis jedis = new Jedis("localhost", 6379);
        jedis.connect();
        try {
            assertThat(jedis.isConnected()).isTrue();

            String key = "debop-mail-using-repository";
            String email = "sunghyouk.bae@gmail.com";

            if (jedis.exists(key))
                Assert.assertEquals(1L, (long) jedis.del(key));

            jedis.setex(key, 60, email);
            assertThat(jedis.exists(key)).isTrue();
            assertThat(jedis.get(key)).isEqualTo(email);
        } finally {
            jedis.disconnect();
        }
    }
}
