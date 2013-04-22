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

package kr.debop4j.data.redis.client;

import redis.clients.jedis.Jedis;

/**
 * kr.debop4j.data.redis.client.RedisAction
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 8. 오후 2:30
 */
public interface RedisAction {

    /**
     * 실행할 함수
     */
    void run(Jedis jedis);
}
