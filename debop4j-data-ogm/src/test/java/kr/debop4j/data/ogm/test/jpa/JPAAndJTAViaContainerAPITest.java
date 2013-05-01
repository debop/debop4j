package kr.debop4j.data.ogm.test.jpa;

import kr.debop4j.data.ogm.test.utils.jpa.JpaTestBase;
import org.fest.assertions.Assertions;
import org.junit.Test;

import javax.persistence.EntityManager;

/**
 * kr.debop4j.data.ogm.test.jpa.JPAAndJTAViaContainerAPITest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 12. 오후 4:50
 */
public class JPAAndJTAViaContainerAPITest extends JpaTestBase {
    @Override
    public Class<?>[] getEntities() {
        return new Class<?>[] { Poem.class };
    }

    @Test
    public void doTest() throws Exception {
        getTransactionManager().begin();
        final EntityManager em = getFactory().createEntityManager();
        Poem poem = new Poem();
        poem.setName("배성혁");
        em.persist(poem);
        getTransactionManager().commit();

        em.clear();

        getTransactionManager().begin();
        poem = em.find(Poem.class, poem.getId());
        Assertions.assertThat(poem).isNotNull();
        Assertions.assertThat(poem.getName()).isEqualTo("배성혁");
        em.remove(poem);
        getTransactionManager().commit();

        em.close();
    }
}
