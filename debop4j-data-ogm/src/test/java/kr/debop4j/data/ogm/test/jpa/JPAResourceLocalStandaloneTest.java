package kr.debop4j.data.ogm.test.jpa;

import kr.debop4j.data.ogm.test.utils.PackagingRule;
import kr.debop4j.data.ogm.test.utils.RequiresTransactionalCapabilitiesRule;
import kr.debop4j.data.ogm.test.utils.TestHelper;
import org.junit.Rule;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.data.ogm.test.jpa.JPAResourceLocalStandaloneTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 12. 오후 2:59
 */
public class JPAResourceLocalStandaloneTest {

    @Rule
    public PackagingRule packaging = new PackagingRule("persistencexml/jpajtastandalone-resourcelocal.xml", Poem.class);

    @Rule
    public RequiresTransactionalCapabilitiesRule transactios = new RequiresTransactionalCapabilitiesRule();

    @Test
    public void jtaStandalone() throws Exception {

        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpajtastandalone");

        try {
            final EntityManager em = emf.createEntityManager();
            try {
                em.getTransaction().begin();
                Poem poem = new Poem();
                poem.setName("L'albatros");
                em.persist(poem);
                em.getTransaction().commit();

                em.clear();

                em.getTransaction().begin();
                Poem poem2 = new Poem();
                poem2.setName("Mazaaaa");
                em.persist(poem2);
                em.flush();
                assertThat(TestHelper.assertNumberOfEntities(2, em)).isTrue();
                em.getTransaction().rollback();

                assertThat(TestHelper.assertNumberOfEntities(1, em)).isTrue();

                em.getTransaction().begin();
                poem = em.find(Poem.class, poem.getId());
                assertThat(poem).isNotNull();
                assertThat(poem.getName()).isEqualTo("L'albatros");
                em.remove(poem);
                poem2 = em.find(Poem.class, poem2.getId());
                assertThat(poem2).isNull();

                em.getTransaction().commit();

            } finally {
                EntityTransaction transaction = em.getTransaction();
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                em.close();
            }
        } finally {
            TestHelper.dropSchemaAndDatabase(emf);
            emf.close();
        }
    }
}
