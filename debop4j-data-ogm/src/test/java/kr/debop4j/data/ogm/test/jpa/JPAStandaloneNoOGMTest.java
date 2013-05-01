package kr.debop4j.data.ogm.test.jpa;

import kr.debop4j.data.ogm.test.utils.PackagingRule;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * kr.debop4j.data.ogm.test.jpa.JPAStandaloneNoOGMTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 12. 오후 2:51
 */
@Slf4j
public class JPAStandaloneNoOGMTest {

    @Rule
    public PackagingRule packaging = new PackagingRule("persistencexml/jpajtastandalone-noogm.xml", Poem.class);

    @Rule
    public ExpectedException error = ExpectedException.none();

    @Test
    public void jtaStandaloneNoOgm() throws Exception {
        // Failure is expected as we did't configure a JDBC connection nor a Dialect
        // (and this would fail only if effectively lading Hibernate OGM without OGM superpowers)
        error.expect(javax.persistence.PersistenceException.class);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpajtastandalone-noogm");
        emf.close(); // should not be reached, but cleanup in case the test fails.
    }
}
