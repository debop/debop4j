package kr.debop4j.data.ogm.test.associations.manytoone;

import kr.debop4j.data.ogm.test.simpleentity.OgmTestBase;
import kr.debop4j.data.ogm.test.utils.TestHelper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * kr.debop4j.data.ogm.test.associations.manytoone.ManyToOneTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 2. 오후 12:08
 */
@Slf4j
public class ManyToOneTest extends OgmTestBase {
    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class<?>[]{
                Jug.class,
                Member.class,
                SalesForce.class,
                SalesGuy.class,
                Beer.class,
                Brewery.class
        };
    }

    @Getter
    private Session session;

    @Override
    public void doBefore() throws Exception {
        super.doBefore();
        session = openSession();
    }

    @Override
    public void doAfter() throws Exception {
        if (session != null)
            session.close();
        super.doAfter();
    }

    @Test
    public void uniDirectionalManyToOne() throws Exception {
        Transaction transaction = session.beginTransaction();

        Jug jug = new Jug("JUG Summer Camp");

        Member emmanuel = new Member("Emmanuel Bernard");
        emmanuel.setMemberOf(jug);

        Member jerome = new Member("Jerome");
        jerome.setMemberOf(jug);

        session.persist(jug);
        session.persist(emmanuel);
        session.persist(jerome);
        session.flush();

        assertThat(TestHelper.assertNumberOfEntities(3, sessions)).isTrue();
        assertThat(TestHelper.assertNumberOfAssociations(0, sessions)).isTrue();

        transaction.commit();

        assertThat(TestHelper.assertNumberOfEntities(3, sessions)).isTrue();
        assertThat(TestHelper.assertNumberOfAssociations(0, sessions)).isTrue();

        session.clear();

        transaction = session.beginTransaction();

        emmanuel = (Member) session.get(Member.class, emmanuel.getId());
        jug = emmanuel.getMemberOf();
        session.delete(emmanuel);

        jerome = (Member) session.get(Member.class, jerome.getId());
        session.delete(jerome);

        session.delete(jug);

        transaction.commit();

        assertThat(TestHelper.assertNumberOfEntities(0, sessions)).isTrue();
        assertThat(TestHelper.assertNumberOfAssociations(0, sessions)).isTrue();

        checkCleanCache();

    }

    @Test
    public void testBidirectionalManyToOneRegular() throws Exception {
        Transaction transaction = session.beginTransaction();

        SalesForce force = new SalesForce("Red Hat");
        session.save(force);

        SalesGuy eric = new SalesGuy();
        eric.setName("Eric");
        eric.setSalesForce(force);
        force.getSalesGuys().add(eric);
        session.save(eric);

        SalesGuy simon = new SalesGuy();
        simon.setName("Simon");
        simon.setSalesForce(force);
        force.getSalesGuys().add(simon);
        session.save(simon);

        transaction.commit();
        session.clear();

        transaction = session.beginTransaction();

        force = (SalesForce) session.get(SalesForce.class, force.getId());
        assertNotNull(force.getSalesGuys());
        assertEquals(2, force.getSalesGuys().size());
        simon = (SalesGuy) session.get(SalesGuy.class, simon.getId());

        // Cascade 때문에
        force.getSalesGuys().remove(simon);
        session.delete(simon);
        transaction.commit();
        session.clear();

        transaction = session.beginTransaction();

        force = (SalesForce) session.get(SalesForce.class, force.getId());
        assertNotNull(force.getSalesGuys());
        assertEquals(1, force.getSalesGuys().size());
        session.delete(force.getSalesGuys().iterator().next());
        session.delete(force);
        transaction.commit();

        checkCleanCache();
    }

    @Test
    public void testBiDirManyToOneInsertUpdateFalse() throws Exception {
        Transaction tx = session.beginTransaction();

        Beer hoegaarden = new Beer();
        Brewery hoeBrewery = new Brewery();
        hoeBrewery.getBeers().add(hoegaarden);
        hoegaarden.setBrewery(hoeBrewery);
        session.persist(hoeBrewery);

        tx.commit();
        session.clear();

        tx = session.beginTransaction();
        hoegaarden = TestHelper.get(session, Beer.class, hoegaarden.getId());
        assertThat(hoegaarden)
                .isNotNull();
        assertThat(hoegaarden.getBrewery())
                .isNotNull();
        assertThat(hoegaarden.getBrewery().getBeers())
                .hasSize(1)
                .containsOnly(hoegaarden);
        Beer citron = new Beer();
        hoeBrewery = hoegaarden.getBrewery();
        hoeBrewery.getBeers().remove(hoegaarden);
        hoeBrewery.getBeers().add(citron);
        citron.setBrewery(hoeBrewery);
        session.delete(hoegaarden);

        tx.commit();
        session.clear();

        tx = session.beginTransaction();

        citron = TestHelper.get(session, Beer.class, citron.getId());
        assertThat(citron.getBrewery().getBeers())
                .hasSize(1)
                .containsOnly(citron);
        hoeBrewery = citron.getBrewery();
        citron.setBrewery(null);
        hoeBrewery.getBeers().clear();
        session.delete(citron);
        session.delete(hoeBrewery);
        tx.commit();

        checkCleanCache();
    }

}
