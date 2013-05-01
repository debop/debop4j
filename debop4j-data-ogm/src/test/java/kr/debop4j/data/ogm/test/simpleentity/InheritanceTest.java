package kr.debop4j.data.ogm.test.simpleentity;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Test;

/**
 * kr.debop4j.data.ogm.test.simpleentity.InheritanceTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 1
 */
public class InheritanceTest extends OgmTestBase {

    @Test
    public void testInheritance() throws Exception {

        final Session session = openSession();
        Transaction transaction = session.beginTransaction();

        Hero h = new Hero();
        h.setName("Spartacus");
        session.persist(h);

        SuperHero sh = new SuperHero();
        sh.setName("Batman");
        sh.setSpecialPower("Technology and samurai technique");
        session.persist(sh);

        transaction.commit();
        session.clear();

        transaction = session.beginTransaction();
        Hero lh = (Hero) session.get(Hero.class, h.getName());
        Assert.assertNotNull(lh);
        Assert.assertEquals(h.getName(), lh.getName());

        SuperHero lsh = (SuperHero) session.get(SuperHero.class, sh.getName());
        Assert.assertNotNull(lsh);
        Assert.assertEquals(sh.getSpecialPower(), lsh.getSpecialPower());

        session.delete(lh);
        session.delete(lsh);

        transaction.commit();
        session.close();
    }

    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class<?>[] {
                Hero.class,
                SuperHero.class
        };
    }
}
