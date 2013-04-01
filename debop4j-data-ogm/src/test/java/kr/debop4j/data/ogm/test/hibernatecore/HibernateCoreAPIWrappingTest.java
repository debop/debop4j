package kr.debop4j.data.ogm.test.hibernatecore;

import kr.debop4j.data.ogm.test.simpleentity.OgmTestBase;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.ogm.hibernatecore.impl.OgmSession;
import org.hibernate.ogm.hibernatecore.impl.OgmSessionFactory;
import org.hibernate.ogm.hibernatecore.impl.OgmSessionFactoryObjectFactory;
import org.junit.Assert;
import org.junit.Test;

import javax.naming.Reference;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.data.ogm.test.hibernatecore.HibernateCoreAPIWrappingTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 1
 */
public class HibernateCoreAPIWrappingTest extends OgmTestBase {

    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class<?>[] { Contact.class };
    }

    @Test
    public void testWrappedFromEntityManagerAPI() throws Exception {

        Session s = openSession();

        Assert.assertTrue(sessions instanceof OgmSessionFactory);

        Assert.assertTrue(s instanceof OgmSession);
        Assert.assertTrue(s.getSessionFactory() instanceof OgmSessionFactory);
        s.close();
    }

    @Test
    public void testJNDIReference() throws Exception {

        Session session = openSession();

        Reference reference = sessions.getReference();
        assertThat(reference.getClassName()).isEqualTo(OgmSessionFactory.class.getName());
        assertThat(reference.getFactoryClassName()).isEqualTo(OgmSessionFactoryObjectFactory.class.getName());
        assertThat(reference.get(0)).isNotNull();
        assertThat(reference.getFactoryClassLocation()).isNull();

        OgmSessionFactoryObjectFactory objFactory = new OgmSessionFactoryObjectFactory();
        SessionFactory factoryFromRegistry = (SessionFactory) objFactory.getObjectInstance(reference, null, null, null);
        assertThat(factoryFromRegistry.getClass()).isEqualTo(OgmSessionFactory.class);
        assertThat(factoryFromRegistry.getReference()).isEqualTo(sessions.getReference());

        session.close();
    }
}
