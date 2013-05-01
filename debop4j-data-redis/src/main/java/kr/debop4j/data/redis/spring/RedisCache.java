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

package kr.debop4j.data.redis.spring;

import org.springframework.cache.Cache;

/**
 * Spring framework에서 제공하는 Cache 를 Redis 를 저장소로 사용하는 Cache로 구현
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 8. 오후 2:08
 */
public class RedisCache implements Cache {

    @Override
    public String getName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object getNativeCache() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ValueWrapper get(Object key) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void put(Object key, Object value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void evict(Object key) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void clear() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
