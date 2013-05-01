package kr.debop4j.data.ogm.test.simpleentity;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * kr.debop4j.data.ogm.test.simpleentity.CRUDTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 1
 */
public class CRUDTest extends OgmTestBase {

    private Session session;
    private Transaction transaction;

    @Override
    public void doBefore() throws Exception {
        super.doBefore();
        session = openSession();
    }

    @Override
    public void doAfter() throws Exception {
        session.close();
        super.doAfter();
    }

    @Test
    public void testSimpleCRUD() throws Exception {

        transaction = session.beginTransaction();
        Hypothesis hyp = new Hypothesis();
        hyp.setId("1234567890");
        hyp.setDescription("NP != P");
        hyp.setPosition(1);
        session.persist(hyp);
        transaction.commit();

        session.clear();

        transaction = session.beginTransaction();
        final Hypothesis loadedHyp = (Hypothesis) session.get(Hypothesis.class, hyp.getId());
        assertNotNull("Cannot load persisted object", loadedHyp);
        assertEquals("persist and load fails", hyp.getDescription(), loadedHyp.getDescription());
        assertEquals("@Column fails", hyp.getPosition(), loadedHyp.getPosition());
        transaction.commit();

        session.clear();

        transaction = session.beginTransaction();
        loadedHyp.setDescription("P != NP");
        session.merge(loadedHyp);
        transaction.commit();

        session.clear();

        transaction = session.beginTransaction();
        Hypothesis secondLoadedHyp = (Hypothesis) session.get(Hypothesis.class, hyp.getId());
        assertEquals("Merge fails", loadedHyp.getDescription(), secondLoadedHyp.getDescription());
        session.delete(secondLoadedHyp);
        transaction.commit();

        session.clear();

        transaction = session.beginTransaction();
        assertNull(session.get(Hypothesis.class, hyp.getId()));
        transaction.commit();
    }

    @Test
    public void testGeneratedValue() throws Exception {

        Transaction transaction = session.beginTransaction();
        Helicopter h = new Helicopter();
        h.setName("Eurocopter");
        session.persist(h);
        transaction.commit();

        session.clear();

        transaction = session.beginTransaction();
        h = (Helicopter) session.get(Helicopter.class, h.getId());
        session.delete(h);
        transaction.commit();
    }

    @Test
    public void performanceLoop() throws Exception {
        long start = 0;
        for (int i = 0; i < 100; i++) {
            if (i % 10 == 0) start = System.nanoTime();
            testSimpleCRUD();
            if (i % 10 == 99) {
                long elapsed = System.nanoTime() - start;
                System.out.printf("%.3E ms for 10000 tests\n", (elapsed) / 1000000f);
            }
        }
    }

    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class<?>[] { Hypothesis.class, Helicopter.class };
    }

}
