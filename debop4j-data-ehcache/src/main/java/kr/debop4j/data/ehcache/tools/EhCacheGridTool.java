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

package kr.debop4j.data.ehcache.tools;

import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.hibernate.ogm.datastore.spi.DefaultDatastoreNames;
import org.hibernate.ogm.grid.EntityKey;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * kr.debop4j.data.ehcache.tools.EhCacheGridTool
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 29
 */
@Slf4j
public class EhCacheGridTool {

    private CacheManager cacheManager;

    /**
     * Instantiates a new EhCacheGridTool
     *
     * @param cacheManager the cache manager
     */
    @Autowired
    public EhCacheGridTool(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * Gets association cache.
     *
     * @return the association cache
     */
    public Cache getAssociationCache() {
        return cacheManager.getCache(DefaultDatastoreNames.ASSOCIATION_STORE);
    }

    /**
     * Gets entity cache.
     *
     * @return the entity cache
     */
    public Cache getEntityCache() {
        return cacheManager.getCache(DefaultDatastoreNames.ENTITY_STORE);
    }

    /**
     * Gets identifier cache.
     *
     * @return the identifier cache
     */
    public Cache getIdentifierCache() {
        return cacheManager.getCache(DefaultDatastoreNames.IDENTIFIER_STORE);
    }

    /**
     * Extract entity tuple.
     *
     * @param key the key
     * @return the map
     */
    @SuppressWarnings( "unchecked" )
    public Map<String, Object> extractEntityTuple(EntityKey key) {
        return (Map<String, Object>) getEntityCache().get(key).getObjectKey();
    }
}
