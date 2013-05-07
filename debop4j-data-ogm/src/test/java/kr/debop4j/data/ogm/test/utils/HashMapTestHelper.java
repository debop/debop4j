package kr.debop4j.data.ogm.test.utils;

import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.ogm.datastore.map.impl.MapDatastoreProvider;
import org.hibernate.ogm.datastore.spi.DatastoreProvider;
import org.hibernate.ogm.grid.AssociationKey;
import org.hibernate.ogm.grid.EntityKey;
import org.hibernate.ogm.grid.RowKey;

import java.util.HashMap;
import java.util.Map;

/** @author Sanne Grinovero <sanne@hibernate.org> (C) 2011 Red Hat Inc. */
public class HashMapTestHelper implements TestableGridDialect {

    @Override
    public boolean assertNumberOfEntities(int numberOfEntities, SessionFactory sessionFactory) {
        return getEntityMap(sessionFactory).size() == numberOfEntities;
    }

    @Override
    public boolean assertNumberOfAssociations(int numberOfAssociations, SessionFactory sessionFactory) {
        return getAssociationCache(sessionFactory).size() == numberOfAssociations;
    }

    @Override
    public Map<String, Object> extractEntityTuple(SessionFactory sessionFactory, EntityKey key) {
        return getEntityMap(sessionFactory).get(key);
    }

    private static Map<EntityKey, Map<String, Object>> getEntityMap(SessionFactory sessionFactory) {
        MapDatastoreProvider castProvider = getProvider(sessionFactory);
        return castProvider.getEntityMap();
    }

    private static MapDatastoreProvider getProvider(SessionFactory sessionFactory) {
        DatastoreProvider provider = ((SessionFactoryImplementor) sessionFactory).getServiceRegistry().getService(DatastoreProvider.class);
        if (!(MapDatastoreProvider.class.isInstance(provider))) {
            throw new RuntimeException("Not testing with MapDatastoreProvider, cannot extract underlying map");
        }
        return MapDatastoreProvider.class.cast(provider);
    }

    private static Map<AssociationKey, Map<RowKey, Map<String, Object>>> getAssociationCache(SessionFactory sessionFactory) {
        MapDatastoreProvider castProvider = getProvider(sessionFactory);
        return castProvider.getAssociationsMap();
    }

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
