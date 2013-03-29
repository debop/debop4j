package kr.debop4j.data.ogm.test.associations.collection.manytomany;

import kr.debop4j.data.ogm.test.simpleentity.OgmTestBase;
import org.hibernate.Session;

/**
 * ManyToManyTest
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 29. 오후 9:10
 */
public class ManyToManyTest extends OgmTestBase {

    public void testManyToMany() {
        Session session = openSession();
    }

    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class<?>[]{
                AccountOwner.class,
                BankAccount.class
        };
    }
}
