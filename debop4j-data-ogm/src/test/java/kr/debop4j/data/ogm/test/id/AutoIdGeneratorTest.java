package kr.debop4j.data.ogm.test.id;

import kr.debop4j.data.ogm.test.simpleentity.OgmTestBase;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.data.ogm.test.id.AutoIdGeneratorTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 2. 오후 2:31
 */
@Slf4j
public class AutoIdGeneratorTest extends OgmTestBase {

    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class<?>[]{ DistributedRevisionControl.class };
    }

    @Test
    public void autoIdentifierGenerator() throws Exception {

        DistributedRevisionControl git = new DistributedRevisionControl("git");
        DistributedRevisionControl bzr = new DistributedRevisionControl("bzr");

        final Session session = openSession();
        Transaction tx = session.beginTransaction();

        try {
            session.persist(git);
            session.persist(bzr);
            tx.commit();

        } catch (Exception ignored) {
            tx.rollback();
        }
        session.clear();

        tx = session.beginTransaction();
        try {
            DistributedRevisionControl dvcs = (DistributedRevisionControl) session.get(DistributedRevisionControl.class, git.getId());
            assertThat(dvcs).isNotNull();
            assertThat(dvcs.getId()).isEqualTo(1);
            session.delete(dvcs);

            dvcs = (DistributedRevisionControl) session.get(DistributedRevisionControl.class, git.getId());
            assertThat(dvcs).isNotNull();
            assertThat(dvcs.getId()).isEqualTo(2);
            session.delete(dvcs);

            tx.commit();
        } catch (Exception ignored) {
            tx.rollback();
        }


        session.close();
    }
}
