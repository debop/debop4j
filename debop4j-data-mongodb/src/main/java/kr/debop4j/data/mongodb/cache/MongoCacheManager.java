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

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collection;

import static kr.debop4j.core.Guard.shouldNotBeNull;

/**
 * MongoCache Manager
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 25 오후 3:14
 */
@Slf4j
public class MongoCacheManager extends AbstractTransactionSupportingCacheManager {

    @Getter private MongoTemplate mongoTemplate;
    @Getter private int expireSeconds;

    /**
     * Instantiates a new MongoCacheManager.
     *
     * @param mongoTemplate {@link MongoTemplate} instance.
     * @param expireSeconds expiration value in second
     */
    public MongoCacheManager(MongoTemplate mongoTemplate, int expireSeconds) {
        shouldNotBeNull(mongoTemplate, "mongoTemplate");
        this.mongoTemplate = mongoTemplate;
        this.expireSeconds = expireSeconds;
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        Collection<Cache> caches = Lists.newArrayList();

        for (String name : getCacheNames()) {
            caches.add(new MongoCache(name, mongoTemplate));
        }
        return caches;
    }

    @Override
    public Cache getCache(String name) {
        synchronized (this) {
            Cache cache = super.getCache(name);
            if (cache == null) {
                if (log.isTraceEnabled()) log.trace("새로운 MongoCache를 생성합니다.");
                cache = new MongoCache(name, mongoTemplate);
                addCache(cache);
            }
            return cache;
        }
    }
}
