package kr.debop4j.data.neo4j.cache;

import org.springframework.cache.Cache;

/**
 * kr.debop4j.data.neo4j.cache.Neo4jCache
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 28
 */
public class Neo4jCache implements Cache {

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
