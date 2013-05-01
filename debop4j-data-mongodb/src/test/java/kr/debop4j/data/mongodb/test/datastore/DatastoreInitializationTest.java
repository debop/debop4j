package kr.debop4j.data.mongodb.test.datastore;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.ogm.datastore.mongodb.Environment;
import org.hibernate.ogm.datastore.mongodb.impl.MongoDBDatastoreProvider;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Properties;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * kr.debop4j.data.mongodb.test.datastore.DatastoreInitializationTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 28
 */
@Slf4j
public class DatastoreInitializationTest {

    @Rule
    public ExpectedException error = ExpectedException.none();

    @Test(expected = HibernateException.class)
    public void authenticationTest() throws Exception {
        Properties props = new Properties();

        props.setProperty(Environment.MONGODB_DATABASE, "test");
        props.setProperty(Environment.MONGODB_USERNAME, "notauser");
        props.setProperty(Environment.MONGODB_PASSWORD, "test");

        MongoDBDatastoreProvider provider = new MongoDBDatastoreProvider();
        provider.configure(props);
        provider.start();
    }

    @Test(expected = HibernateException.class)
    public void connectionErrorWrappedInHibernateException() throws Exception {
        Properties props = new Properties();
        props.load(this.getClass().getClassLoader().getResourceAsStream("hibernate.properties"));

        // 알 수 없는 서버
        props.setProperty(Environment.MONGODB_HOST, "203.0.113.1");
        MongoDBDatastoreProvider provider = new MongoDBDatastoreProvider();
        provider.configure(props);
        provider.start();
    }

    @Test
    public void connectionTimeoutTest() throws Exception {
        Properties props = new Properties();
        props.load(this.getClass().getClassLoader().getResourceAsStream("hibernate.properties"));

        String host = "203.0.113.1";

        // 알 수 없는 서버
        props.setProperty(Environment.MONGODB_HOST, host);
        props.setProperty(Environment.MONGODB_TIMEOUT, "30");

        MongoDBDatastoreProvider provider = new MongoDBDatastoreProvider();

        /*
         * To be sure, the test passes on slow / busy machines the hole
		 * operation should not take more than 3 seconds.
		 */
        final long estimateSpentTime = 3L * 1000L * 1000L * 1000L;
        provider.configure(props);

        Exception exception = null;
        final long start = System.nanoTime();
        try {
            provider.start();
        } catch (Exception e) {
            exception = e;
            assertThat(System.nanoTime() - start).isLessThanOrEqualTo(estimateSpentTime);
        }
        if (exception == null) {
            fail("The expected exception has not been raised, a MongoDB instance runs on " + host);
        }
    }

}
