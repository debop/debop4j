package kr.debop4j.ogm.tools.ehcache;

import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.hibernate.ogm.datastore.spi.DefaultDatastoreNames;
import org.hibernate.ogm.grid.EntityKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * kr.debop4j.ogm.tools.ehcache.EhcacheTool
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 23. 오후 10:56
 */
@Configuration
@Slf4j
public class EhcacheTool {

    @Autowired
    CacheManager cacheManager;

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
