package kr.debop4j.data.ehcache.ogm.test.utils;

import kr.debop4j.data.ogm.test.utils.TestableGridDialect;
import net.sf.ehcache.Cache;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.ogm.datastore.ehcache.impl.EhcacheDatastoreProvider;
import org.hibernate.ogm.datastore.spi.DatastoreProvider;
import org.hibernate.ogm.grid.EntityKey;

import java.util.Map;

import static org.hibernate.ogm.datastore.spi.DefaultDatastoreNames.ASSOCIATION_STORE;
import static org.hibernate.ogm.datastore.spi.DefaultDatastoreNames.ENTITY_STORE;

/**
 * kr.debop4j.data.ehcache.ogm.test.utils.EhcacheTestHelper
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 29. 오후 10:03
 */
public class EhcacheTestHelper implements TestableGridDialect {
    @Override
    public boolean assertNumberOfEntities(int numberOfEntities, SessionFactory sessionFactory) {
        return getEntityCache(sessionFactory).getSize() == numberOfEntities;
    }

    @Override
    public boolean assertNumberOfAssociations(int numberOfAssociations, SessionFactory sessionFactory) {
        return getAssociationCache(sessionFactory).getSize() == numberOfAssociations;
    }

    @Override
    public Map<String, Object> extractEntityTuple(SessionFactory sessionFactory, EntityKey key) {
        return (Map) getEntityCache(sessionFactory).get(key).getValue();
    }

    private static Cache getEntityCache(SessionFactory sessionFactory) {
        EhcacheDatastoreProvider castProvider = getProvider(sessionFactory);
        return castProvider.getCacheManager().getCache(ENTITY_STORE);
    }

    private static EhcacheDatastoreProvider getProvider(SessionFactory sessionFactory) {
        DatastoreProvider provider = ((SessionFactoryImplementor) sessionFactory).getServiceRegistry()
                .getService(DatastoreProvider.class);
        if (!(EhcacheDatastoreProvider.class.isInstance(provider))) {
            throw new RuntimeException("Not testing with Ehcache, cannot extract underlying cache");
        }
        return EhcacheDatastoreProvider.class.cast(provider);
    }

    private static Cache getAssociationCache(SessionFactory sessionFactory) {
        EhcacheDatastoreProvider castProvider = getProvider(sessionFactory);
        return castProvider.getCacheManager().getCache(ASSOCIATION_STORE);
    }

    /**
     * TODO - EHCache _is_ transactional. Turn this on. We could turn on XA or Local.
     * Local will be faster. We will pick this up from the cache config.
     *
     * @return
     */
    @Override
    public boolean backendSupportsTransactions() {
        return false;
    }

    @Override
    public void dropSchemaAndDatabase(SessionFactory sessionFactory) {
        //Nothing to do
    }

    @Override
    public Map<String, String> getEnvironmentProperties() {
        return null;
    }
}
