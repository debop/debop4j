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

package kr.debop4j.data.mongodb.cache;

import kr.debop4j.core.Guard;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.io.Serializable;

/**
 * MongoDB를 저장소로 사용하는 캐시
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 *         13. 3. 25 오후 3:14
 */
@Slf4j
public class MongoCache implements Cache {

    private String name;
    private MongoTemplate mongoTemplate;

    public MongoCache(String name, MongoTemplate mongoTemplate) {
        Guard.shouldNotBeEmpty(name, "name");
        Guard.shouldNotBeNull(mongoTemplate, "mongoTemplate");

        this.name = name;
        this.mongoTemplate = mongoTemplate;

        if (log.isDebugEnabled())
            log.debug("MongoCache를 생성합니다. name=[{}], mongodb=[{}]", name, mongoTemplate.getDb().getName());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return mongoTemplate;
    }

    @Override
    public ValueWrapper get(Object key) {
        assert key != null;

        CacheItem item = mongoTemplate.findOne(new Query(Criteria.where("key").is(key)),
                                               CacheItem.class,
                                               name);

        Object result = null;
        if (item != null) {
            result = item.getValue();
            if (log.isDebugEnabled())
                log.debug("캐시 값을 로드했습니다. key=[{}], value=[{}]", key, result);
        }

        SimpleValueWrapper wrapper = null;
        if (result != null)
            wrapper = new SimpleValueWrapper(result);
        return wrapper;
    }

    @Override
    public void put(Object key, Object value) {
        assert key != null;

        if (!mongoTemplate.collectionExists(name))
            mongoTemplate.createCollection(name);

        if (log.isDebugEnabled())
            log.debug("캐시에 값을 저장합니다. key=[{}], value=[{}]", key, value);

        if (get(key) == null)
            mongoTemplate.insert(new CacheItem(key, value), name);
        else
            mongoTemplate.upsert(new Query(Criteria.where("key").is(key)),
                                 Update.update("value", value),
                                 name);
    }

    @Override
    public void evict(Object key) {
        assert key != null;
        mongoTemplate.remove(new Query(Criteria.where("key").is(key)), name);
    }

    @Override
    public void clear() {
        mongoTemplate.dropCollection(name);
    }

    @Getter
    @Setter
    public static class CacheItem implements Serializable {

        private static final long serialVersionUID = 6930164833651186483L;

        private Object key;
        private Object value;

        public CacheItem() { this(null, null); }

        public CacheItem(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "CacheItem@{key=" + key + ", value=" + value + "}";
        }
    }
}
