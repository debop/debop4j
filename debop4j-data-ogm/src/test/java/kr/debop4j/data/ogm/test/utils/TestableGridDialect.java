package kr.debop4j.data.ogm.test.utils;

import org.hibernate.SessionFactory;
import org.hibernate.ogm.grid.EntityKey;

import java.util.Map;

/**
 * TestableGridDialect
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 29. 오후 4:41
 */
public interface TestableGridDialect {
    /**
     * Check that the number of entities present matches expectations
     *
     * @param sessionFactory
     */
    boolean assertNumberOfEntities(int numberOfEntities, SessionFactory sessionFactory);

    /**
     * Check that the number of entities present matches expectations
     *
     * @param sessionFactory
     */
    boolean assertNumberOfAssociations(int numberOfAssociations, SessionFactory sessionFactory);

    /**
     * Loads a specific entity tuple directly from the data store by entity key
     *
     * @param sessionFactory
     * @param key
     * @return the loaded tuple, or null of nothing was found
     */
    Map<String, Object> extractEntityTuple(SessionFactory sessionFactory, EntityKey key);

    /**
     * Returning false will disable all tests which verify transaction isolation or rollback capabilities.
     * No "production" datastore should return false unless its limitation is properly documented.
     *
     * @return true if the datastore is expected to commit/rollback properly
     */
    boolean backendSupportsTransactions();

    /**
     * Used to clean up all the stored data. The cleaning can be done by dropping
     * the database and/or the schema.
     * Each implementor can so define its own way to delete all data inserted by
     * the test and remove the schema if that applies.
     *
     * @param sessionFactory
     */
    void dropSchemaAndDatabase(SessionFactory sessionFactory);


    /**
     * Properties that needs to be overridden in configuration for tests to run
     * This is typical of the host and port defined using an environment variable.
     */
    Map<String, String> getEnvironmentProperties();
}
