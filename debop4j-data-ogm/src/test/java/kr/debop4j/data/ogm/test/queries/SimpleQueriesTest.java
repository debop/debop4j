package kr.debop4j.data.ogm.test.queries;

import kr.debop4j.data.ogm.test.utils.SessionFactoryRule;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.data.ogm.test.queries.SimpleQueriesTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 1
 */
@Slf4j
public class SimpleQueriesTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @ClassRule
    public static final SessionFactoryRule sessions = new SessionFactoryRule(Hypothesis.class,
                                                                             Helicopter.class);

    @Test
    public void simpleQueries() throws Exception {
        final Session session = sessions.openSession();

        String hypothesiFullName = Hypothesis.class.getName();

        assertQuery(session, 4, session.createQuery(
                "from Hypothesis"));
        assertQuery(session, 4, session.createQuery(
                "from " + hypothesiFullName));
        assertQuery(session, 1, session.createQuery(
                "from Helicopter"));
        assertQuery(session, 5, session.createQuery(
                "from java.lang.Object"));
    }

    @Test
    public void testFailingQuery() {
        final Session session = sessions.openSession();
        thrown.expect(HibernateException.class);
        thrown.expectMessage("OGM000024");
        try {
            assertQuery(session, 4, session.createQuery(
                    "from Object")); //Illegal query
        } finally {
            session.close();
        }
    }

    @Test
    public void testConstantParameterQueries() throws Exception {
        final Session session = sessions.openSession();

        assertQuery(session, 1, session.createQuery(
                "from Hypothesis h where h.description = 'stuff works'"));
        session.close();
    }

    @Test
    public void testParametricQueries() throws Exception {
        final Session session = sessions.openSession();

        Query query = session
                .createQuery("from Hypothesis h where h.description = :myParam")
                .setString("myParam", "stuff works");
        assertQuery(session, 1, query);
        session.close();
    }

    private void assertQuery(final Session session, final int expectedSize, final Query testedQuery) {
        Transaction transaction = session.beginTransaction();
        List list = testedQuery.list();
        try {
            assertThat(list).as("Query failed").hasSize(expectedSize);
        } finally {
            transaction.commit();
            session.clear();
        }
    }

    @BeforeClass
    public static void setUp() throws Exception {
        final Session session = sessions.openSession();
        Transaction transaction = session.beginTransaction();

        if (log.isInfoEnabled())
            log.info("예제용 데이터 추가");

        Hypothesis socrates = new Hypothesis();
        socrates.setId("13");
        socrates.setDescription("There are more than two dimensions over the shadows we see out of the cave");
        socrates.setPosition(1);
        session.persist(socrates);

        Hypothesis peano = new Hypothesis();
        peano.setId("14");
        peano.setDescription("Peano's curve and then Hilbert's space filling curve proof the connection from mono-dimensional to bi-dimensional space");
        peano.setPosition(2);
        session.persist(peano);

        Hypothesis sanne = new Hypothesis();
        sanne.setId("15");
        sanne.setDescription("Hilbert's proof of connection to 2 dimensions can be induced to reason on N dimensions");
        sanne.setPosition(3);
        session.persist(sanne);

        Hypothesis shortOne = new Hypothesis();
        shortOne.setId("16");
        shortOne.setDescription("stuff works");
        shortOne.setPosition(4);
        session.persist(shortOne);

        Helicopter helicopter = new Helicopter();
        helicopter.setName("No creative clue ");
        session.persist(helicopter);

        transaction.commit();
        session.close();
    }
}
