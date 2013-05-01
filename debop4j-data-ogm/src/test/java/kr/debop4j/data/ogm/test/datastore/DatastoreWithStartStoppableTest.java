package kr.debop4j.data.ogm.test.datastore;

import kr.debop4j.data.ogm.test.simpleentity.OgmTestBase;
import org.hibernate.SessionFactory;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.data.ogm.test.datastore.DatastoreWithStartStoppableTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 1
 */
public class DatastoreWithStartStoppableTest extends OgmTestBase {

    @Test
    public void testObserver() throws Exception {
        try {
            openSession();
            SessionFactory factory = sessions;

        } catch (RuntimeException e) {
            assertThat(e.getCause()).isNotNull();
            assertThat(e.getCause().getMessage()).isEqualTo("STARTED!");
        }
    }

    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class<?>[0];
    }
}
