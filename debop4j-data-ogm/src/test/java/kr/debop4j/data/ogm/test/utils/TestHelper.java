package kr.debop4j.data.ogm.test.utils;

import com.arjuna.ats.arjuna.coordinator.TxControl;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.hibernate.ogm.grid.EntityKey;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * TestHelper
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 29. 오후 4:42
 */
@Slf4j
public class TestHelper {
    //private static final Log log = LoggerFactory.make();
    private static final TestableGridDialect helper = createStoreSpecificHelper();

    static {
        //set 5 hours timeout on transactions: enough for debug, but not too high in case of CI problems.
        TxControl.setDefaultTimeout(60 * 60 * 2);
    }

    public static boolean assertNumberOfEntities(int numberOfEntities, EntityManager em) {
        return assertNumberOfEntities(numberOfEntities, em.unwrap(Session.class));
    }

    private static TestableGridDialect createStoreSpecificHelper() {
        for (GridDialectType gridType : GridDialectType.values()) {
            Class<?> classForName = gridType.loadTestableGridDialectClass();
            if (classForName != null) {
                try {
                    TestableGridDialect attempt = (TestableGridDialect) classForName.newInstance();
                    log.debug("Using TestGridDialect [{}]", classForName);
                    return attempt;
                } catch (Exception e) {
                    //but other errors are not expected:
                    log.error("Could not load TestGridDialect by name from [{}]", gridType);
                    log.error("", e);
                }
            }
        }
        return new HashMapTestHelper();
    }

    public static GridDialectType getCurrentDialectType() {
        return GridDialectType.valueFromHelperClass(helper.getClass());
    }

    public static boolean assertNumberOfEntities(int numberOfEntities, Session session) {
        return assertNumberOfEntities(numberOfEntities, session.getSessionFactory());
    }

    public static boolean assertNumberOfEntities(int numberOfEntities, SessionFactory sessionFactory) {
        return helper.assertNumberOfEntities(numberOfEntities, sessionFactory);
    }

    public static Map extractEntityTuple(SessionFactory sessionFactory, EntityKey key) {
        return helper.extractEntityTuple(sessionFactory, key);
    }

    public static boolean assertNumberOfAssociations(int numberOfAssociations, SessionFactory sessionFactory) {
        boolean result = helper.assertNumberOfAssociations(numberOfAssociations, sessionFactory);
        return result;
    }

    public static boolean backendSupportsTransactions() {
        return helper.backendSupportsTransactions();
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Session session, Class<T> clazz, Serializable id) {
        return (T) session.get(clazz, id);
    }

    public static void dropSchemaAndDatabase(Session session) {
        if (session != null) {
            dropSchemaAndDatabase(session.getSessionFactory());
        }
    }

    public static void dropSchemaAndDatabase(EntityManagerFactory emf) {
        dropSchemaAndDatabase(((HibernateEntityManagerFactory) emf).getSessionFactory());
    }

    public static void dropSchemaAndDatabase(SessionFactory sessionFactory) {
        //if the factory is closed, we don't have access to the service registry
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            try {
                helper.dropSchemaAndDatabase(sessionFactory);
            } catch (Exception e) {
                TestHelper.log.warn("Exception while dropping schema and database in test", e);
            }
        }
    }

    public static Map<String, String> getEnvironmentProperties() {
        Map<String, String> environmentProperties = helper.getEnvironmentProperties();
        return environmentProperties == null ? new HashMap<String, String>(0) : environmentProperties;
    }

    public static void initializeHelpers() {
        // just to make sure helper is initialized
    }
}
