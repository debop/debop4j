package kr.debop4j.data.mongodb.test.utils;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import kr.debop4j.data.ogm.test.utils.TestableGridDialect;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.common.AssertionFailure;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.ogm.datastore.mongodb.AssociationStorage;
import org.hibernate.ogm.datastore.mongodb.Environment;
import org.hibernate.ogm.datastore.mongodb.impl.MongoDBDatastoreProvider;
import org.hibernate.ogm.datastore.spi.DatastoreProvider;
import org.hibernate.ogm.dialect.mongodb.MongoDBDialect;
import org.hibernate.ogm.grid.EntityKey;
import org.hibernate.ogm.logging.mongodb.impl.Log;
import org.hibernate.ogm.logging.mongodb.impl.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * kr.debop4j.data.mongodb.test.utils.MongoDBTestHelper
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 10. 오후 9:50
 */
public class MongoDBTestHelper implements TestableGridDialect {

    private static final Log log = LoggerFactory.getLogger();

    static {
        // Read host and port from environment variable
        // Maven's surefire plugin set it to the string 'null'
        String mongoHostName = System.getenv("MONGODB_HOSTNAME");
        if (isNotNull(mongoHostName)) {
            System.getProperties().setProperty(Environment.MONGODB_HOST, mongoHostName);
        }
        String mongoPort = System.getenv("MONGODB_PORT");
        if (isNotNull(mongoPort)) {
            System.getProperties().setProperty(Environment.MONGODB_PORT, mongoPort);
        }
    }

    private static boolean isNotNull(String mongoHostName) {
        return mongoHostName != null && mongoHostName.length() > 0 && !"null".equals(mongoHostName);
    }

    @Override
    public boolean assertNumberOfEntities(int numberOfEntities, SessionFactory sessionFactory) {
        MongoDBDatastoreProvider provider = MongoDBTestHelper.getProvider(sessionFactory);
        AssociationStorage storage = provider.getAssociationStorage();
        DB db = provider.getDatabase();
        int count = 0;
        for (String collectionName : db.getCollectionNames()) {
            if (collectionName.startsWith("system."))
                continue;

            if (storage == AssociationStorage.GLOBAL_COLLECTION
                    && collectionName.equals(Environment.MONGODB_DEFAULT_ASSOCIATION_STORE))
                continue;

            if (storage == AssociationStorage.COLLECTION
                    && collectionName.startsWith(MongoDBDialect.ASSOCIATIONS_COLLECTION_PREFIX))
                continue;


            count += db.getCollection(collectionName).count();
        }
        return count == numberOfEntities;
    }

    @Override
    public boolean assertNumberOfAssociations(int numberOfAssociations, SessionFactory sessionFactory) {
        MongoDBDatastoreProvider provider = MongoDBTestHelper.getProvider(sessionFactory);
        AssociationStorage assocStorage = provider.getAssociationStorage();
        DB db = provider.getDatabase();

        if (assocStorage == AssociationStorage.IN_ENTITY) {
            return true; //FIXME find a way to test that, maybe with some map reduce magic?
        } else if (assocStorage == AssociationStorage.GLOBAL_COLLECTION) {
            return db.getCollection(Environment.MONGODB_DEFAULT_ASSOCIATION_STORE).count() == numberOfAssociations;
        } else if (assocStorage == AssociationStorage.COLLECTION) {
            int count = 0;
            for (String collectionName : db.getCollectionNames()) {
                if (assocStorage == AssociationStorage.COLLECTION
                        && collectionName.startsWith(MongoDBDialect.ASSOCIATIONS_COLLECTION_PREFIX)) {
                    count += db.getCollection(collectionName).count();
                }
            }
            return count == numberOfAssociations;
        } else {
            throw new AssertionFailure("Unknown AssociationStorage approach: " + assocStorage);
        }
    }

    @Override
    public Map<String, Object> extractEntityTuple(SessionFactory sessionFactory, EntityKey key) {
        MongoDBDatastoreProvider provider = MongoDBTestHelper.getProvider(sessionFactory);
        DBObject finder = new BasicDBObject(MongoDBDialect.ID_FIELDNAME, key.getColumnValues()[0]);
        DBObject result = provider.getDatabase().getCollection(key.getTable()).findOne(finder);
        replaceIdentifierColumnName(result, key);
        return result.toMap();
    }

    /**
     * The MongoDB dialect replaces the name of the column identifier, so when the tuple is extracted from the db
     * we replace the column name of the identifier with the original one.
     * We are assuming the identifier is not embedded and is a single property.
     */
    private void replaceIdentifierColumnName(DBObject result, EntityKey key) {
        Object idValue = result.get(MongoDBDialect.ID_FIELDNAME);
        result.removeField(MongoDBDialect.ID_FIELDNAME);
        result.put(key.getColumnNames()[0], idValue);
    }

    @Override
    public boolean backendSupportsTransactions() {
        return false;
    }

    private static MongoDBDatastoreProvider getProvider(SessionFactory sessionFactory) {
        DatastoreProvider provider = ((SessionFactoryImplementor) sessionFactory).getServiceRegistry().getService(
                DatastoreProvider.class);
        if (!(MongoDBDatastoreProvider.class.isInstance(provider))) {
            throw new RuntimeException("Not testing with MongoDB, cannot extract underlying cache");
        }
        return MongoDBDatastoreProvider.class.cast(provider);
    }

    @Override
    public void dropSchemaAndDatabase(SessionFactory sessionFactory) {
        MongoDBDatastoreProvider provider = getProvider(sessionFactory);
        try {
            log.info("MongoDB의 provider를 얻어 database를 삭제합니다...");
            provider.getDatabase().dropDatabase();
        } catch (MongoException ex) {
            throw log.unableToDropDatabase(ex, provider.getDatabase().getName());
        }
    }

    @Override
    public Map<String, String> getEnvironmentProperties() {

        //read variables from the System properties set in the static initializer
        Map<String, String> props = new HashMap<String, String>(2);
        copyFromSystemPropertiesToLocalEnvironment(Environment.MONGODB_HOST, props);
        copyFromSystemPropertiesToLocalEnvironment(Environment.MONGODB_PORT, props);
        copyFromSystemPropertiesToLocalEnvironment(Environment.MONGODB_USERNAME, props);
        copyFromSystemPropertiesToLocalEnvironment(Environment.MONGODB_PASSWORD, props);

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

    private void copyFromSystemPropertiesToLocalEnvironment(String environmentVariableName, Map<String, String> envProps) {
        String value = System.getProperties().getProperty(environmentVariableName);
        if (value != null && value.length() > 0) {
            envProps.put(environmentVariableName, value);
        }
    }
}
