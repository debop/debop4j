package kr.debop4j.data.ogm.test.jpa;

import kr.debop4j.data.ogm.test.utils.PackagingRule;
import kr.debop4j.data.ogm.test.utils.TestHelper;
import kr.debop4j.data.ogm.test.utils.jpa.JpaTestBase;
import lombok.extern.slf4j.Slf4j;
import org.fest.assertions.Assertions;
import org.junit.Rule;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.TransactionManager;

/**
 * kr.debop4j.data.ogm.test.jpa.JPAStandaloneTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 12. 오후 1:33
 */
@Slf4j
public class JPAStandaloneTest {

    @Rule
    public PackagingRule packagingRule = new PackagingRule("persistencexml/jpajtastandalone.xml", Poem.class);

    @Test
    public void jtaStandAlone() throws Exception {

        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpajtastandalone", TestHelper.getEnvironmentProperties());
        TransactionManager transactionManager = JpaTestBase.extractJBossTransactionManager(emf);

        transactionManager.begin();
        final EntityManager em = emf.createEntityManager();
        Poem poem = new Poem();
        poem.setName("L'albatros");
        em.persist(poem);
        transactionManager.commit();

        em.clear();

        transactionManager.begin();
        poem = em.find(Poem.class, poem.getId());
        Assertions.assertThat(poem).isNotNull();
        Assertions.assertThat(poem.getName()).isEqualTo("L'albatros");
        em.remove(poem);
        transactionManager.commit();
    }
}
