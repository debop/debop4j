package kr.debop4j.data.ogm.test.id;

import kr.debop4j.data.ogm.test.utils.jpa.JpaTestBase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.persistence.EntityManager;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.data.ogm.test.id.AutoIdGeneratorTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 2. 오후 2:31
 */
@Slf4j
public class AutoIdGeneratorTest extends JpaTestBase {

    @Override
    public Class<?>[] getEntities() {
        return new Class<?>[]{ DistributedRevisionControl.class };
    }

    @Test
    public void autoIdentifierGenerator() throws Exception {

        DistributedRevisionControl git = new DistributedRevisionControl();
        DistributedRevisionControl bzr = new DistributedRevisionControl();
        getTransactionManager().begin();
        final EntityManager em = getFactory().createEntityManager();
        boolean operationSuccessfull = false;
        try {
            git.setName("Git");
            em.persist(git);

            bzr.setName("Bazaar");
            em.persist(bzr);
            operationSuccessfull = true;
        } finally {
            commitOrRollback(operationSuccessfull);
        }

        em.clear();
        getTransactionManager().begin();
        operationSuccessfull = false;
        try {
            DistributedRevisionControl dvcs = em.find(DistributedRevisionControl.class, git.getId());
            assertThat(dvcs).isNotNull();
            assertThat(dvcs.getId()).isEqualTo(1);
            em.remove(dvcs);

            dvcs = em.find(DistributedRevisionControl.class, bzr.getId());
            assertThat(dvcs).isNotNull();
            assertThat(dvcs.getId()).isEqualTo(2);
            operationSuccessfull = true;
        } finally {
            commitOrRollback(operationSuccessfull);
        }
        em.close();
    }
}
