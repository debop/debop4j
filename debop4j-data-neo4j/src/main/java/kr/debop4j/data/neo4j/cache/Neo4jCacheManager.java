package kr.debop4j.data.neo4j.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;

import java.util.Collection;

/**
 * kr.debop4j.data.neo4j.cache.Neo4jCacheManager
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 28
 */
public class Neo4jCacheManager extends AbstractCacheManager {

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
