package kr.debop4j.nosql.ehcache.tools;

import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.hibernate.ogm.datastore.spi.DefaultDatastoreNames;
import org.hibernate.ogm.grid.EntityKey;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * kr.debop4j.nosql.ehcache.tools.EhCacheGridTool
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 29
 */
@Slf4j
public class EhCacheGridTool {

    private CacheManager cacheManager;

    @Autowired
    public EhCacheGridTool(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public Cache getAssociationCache() {
        return cacheManager.getCache(DefaultDatastoreNames.ASSOCIATION_STORE);
    }

    public Cache getEntityCache() {
        return cacheManager.getCache(DefaultDatastoreNames.ENTITY_STORE);
    }

    public Cache getIdentifierCache() {
        return cacheManager.getCache(DefaultDatastoreNames.IDENTIFIER_STORE);
    }

    public Map<String, Object> extractEntityTuple(EntityKey key) {
        return (Map<String, Object>) getEntityCache().get(key).getObjectKey();
    }
}
