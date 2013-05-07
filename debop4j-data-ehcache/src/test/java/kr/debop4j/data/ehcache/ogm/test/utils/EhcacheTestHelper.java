package kr.debop4j.data.ehcache.ogm.test.utils;

import kr.debop4j.data.ogm.test.utils.TestableGridDialect;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.ogm.datastore.ehcache.impl.EhcacheDatastoreProvider;
import org.hibernate.ogm.datastore.spi.DatastoreProvider;
import org.hibernate.ogm.grid.EntityKey;

import java.util.HashMap;
import java.util.Map;

import static org.hibernate.ogm.datastore.spi.DefaultDatastoreNames.ASSOCIATION_STORE;
import static org.hibernate.ogm.datastore.spi.DefaultDatastoreNames.ENTITY_STORE;

/**
 * kr.debop4j.data.ehcache.ogm.test.utils.EhcacheTestHelper
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 29. 오후 10:03
 */
@Slf4j
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
    @SuppressWarnings("unchecked")
    public Map<String, Object> extractEntityTuple(SessionFactory sessionFactory, EntityKey key) {
        return (Map<String, Object>) getEntityCache(sessionFactory).get(key).getObjectValue();
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

        Map<String, String> props = new HashMap<String, String>();

        // hibernate-search 환경설정
        props.put("hibernate.search.default.indexmanager", "near-real-time");
        props.put("hibernate.search.default.directory_provider", "filesystem");
        props.put("hibernate.search.default.indexBase", ".lucene/indexes");
        props.put("hibernate.search.default.locking_strategy", "simple");

        // hibernate-search index worker settings
        props.put("hibernate.search.worker.execution", "async");
        props.put("hibernate.search.worker.thread_pool.size", "8");
        props.put("hibernate.search.worker.buffer_queue.max", "1000000");

        // hibernate-search performance settings
        props.put("hibernate.search.default.indexwriter.max_buffered_doc", "true");
        props.put("hibernate.search.default.indexwriter.max_merge_docs", "100");
        props.put("hibernate.search.default.indexwriter.merge_factor", "20");
        props.put("hibernate.search.default.indexwriter.term_index_interval", "default");
        props.put("hibernate.search.default.indexwriter.ram_buffer_size", "2048");
        props.put("hibernate.search.default.exclusive_index_use", "true");

        return props;
    }
}
