package kr.debop4j.data.ogm.test.jpa;

import kr.debop4j.data.ogm.test.utils.PackagingRule;
import kr.debop4j.data.ogm.test.utils.TestHelper;
import kr.debop4j.data.ogm.test.utils.jpa.JpaTestBase;
import org.hibernate.ogm.jpa.impl.OgmEntityManager;
import org.hibernate.ogm.jpa.impl.OgmEntityManagerFactory;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import java.util.HashMap;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.data.ogm.test.jpa.JPAAPIWrappingTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 12. 오후 3:38
 */
public class JPAAPIWrappingTest extends JpaTestBase {

    @Rule
    public PackagingRule packaging = new PackagingRule("persistencexml/jpajtastandalone.xml", Poem.class);
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void wrappedStandalone() throws Exception {
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpajtastandalone", TestHelper.getEnvironmentProperties());
        assertThat(emf.getClass()).isEqualTo(OgmEntityManagerFactory.class);

        EntityManager em = emf.createEntityManager();
        assertThat(em.getClass()).isEqualTo(OgmEntityManager.class);
        em.close();

        em = emf.createEntityManager(new HashMap());
        assertThat(em.getClass()).isEqualTo(OgmEntityManager.class);
        em.close();

        emf.close();
    }

    @Test
    public void undefiendPU() throws Exception {
        thrown.expect(PersistenceException.class);
        Persistence.createEntityManagerFactory("does-not-exist-PU");
    }

    @Test
    public void wrapInContainer() throws Exception {
        assertThat(getFactory().getClass()).isEqualTo(OgmEntityManagerFactory.class);

        EntityManager entityManager = getFactory().createEntityManager();
        assertThat(entityManager.getClass()).isEqualTo(OgmEntityManager.class);
        entityManager.close();

        entityManager = getFactory().createEntityManager(new HashMap());
        assertThat(entityManager.getClass()).isEqualTo(OgmEntityManager.class);
        entityManager.close();
    }

    @Override
    public Class<?>[] getEntities() {
        return new Class<?>[]{ Poem.class };
    }
}
