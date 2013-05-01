package kr.debop4j.data.ogm.test.associations.onetoone;

import kr.debop4j.data.ogm.test.simpleentity.OgmTestBase;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * kr.debop4j.data.ogm.test.associations.onetoone.OneToOneTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 1
 */
@Slf4j
public class OneToOneTest extends OgmTestBase {

    @Test
    public void unidirectionalManyToOne() throws Exception {

        final Session session = openSession();
        Transaction transaction = session.beginTransaction();
        Horse horse = new Horse();
        horse.setName("Palefrenier");
        Cavalier cavalier = new Cavalier();
        cavalier.setName("Caroline");
        cavalier.setHorse(horse);
        session.persist(horse);
        session.persist(cavalier);
        transaction.commit();
        session.clear();

        transaction = session.beginTransaction();
        cavalier = (Cavalier) session.get(Cavalier.class, cavalier.getId());
        horse = cavalier.getHorse();
        session.delete(cavalier);
        session.delete(horse);
        transaction.commit();

        session.close();

        checkCleanCache();
    }

    @Test
    public void unidirectionalOneToOne() throws Exception {
        final Session session = openSession();
        Transaction transaction = session.beginTransaction();

        Vehicle vehicule = new Vehicle();
        vehicule.setBrand("Mercedes");
        Wheel wheel = new Wheel();
        wheel.setVehicle(vehicule);
        session.persist(vehicule);
        session.persist(wheel);

        transaction.commit();
        session.clear();

        transaction = session.beginTransaction();

        if (log.isInfoEnabled())
            log.info("Weel id=[{}]", wheel.getId());

        wheel = (Wheel) session.get(Wheel.class, wheel.getId());
        vehicule = wheel.getVehicle();
        session.delete(wheel);
        session.delete(vehicule);
        transaction.commit();
        session.close();
    }

    @Test
    public void bidirectionalManyToOne() throws Exception {

        final Session session = openSession();
        Transaction transaction = session.beginTransaction();
        Husband husband = new Husband();
        husband.setName("Alex");
        Wife wife = new Wife();
        wife.setName("Bea");
        husband.setWife(wife);
        wife.setHusband(husband);
        session.persist(husband);
        session.persist(wife);
        transaction.commit();
        session.clear();

        transaction = session.beginTransaction();
        husband = (Husband) session.get(Husband.class, husband.getId());
        assertNotNull(husband);
        assertNotNull(husband.getWife());
        session.clear();
        wife = (Wife) session.get(Wife.class, wife.getId());
        assertNotNull(wife);
        husband = wife.getHusband();
        assertNotNull(husband);
        Wife bea2 = new Wife();
        session.persist(bea2);
        bea2.setName("Still Bea");
        husband.setWife(bea2);
        wife.setHusband(null);
        bea2.setHusband(husband);
        transaction.commit();
        session.clear();

        transaction = session.beginTransaction();
        husband = (Husband) session.get(Husband.class, husband.getId());
        assertNotNull(husband);
        assertNotNull(husband.getWife());
        session.clear();
        wife = (Wife) session.get(Wife.class, wife.getId());
        assertNotNull(wife);
        assertNull(wife.getHusband());
        session.delete(wife);
        bea2 = (Wife) session.get(Wife.class, bea2.getId());
        assertNotNull(bea2);
        husband = bea2.getHusband();
        assertNotNull(husband);
        bea2.setHusband(null);
        husband.setWife(null);
        session.delete(husband);
        session.delete(wife);
        transaction.commit();
        session.close();
    }

    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class<?>[] {
                Horse.class,
                Cavalier.class,
                Vehicle.class,
                Wheel.class,
                Husband.class,
                Wife.class
        };
    }
}
