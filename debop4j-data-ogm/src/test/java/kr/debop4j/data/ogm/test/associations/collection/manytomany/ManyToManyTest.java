package kr.debop4j.data.ogm.test.associations.collection.manytomany;

import kr.debop4j.data.ogm.test.simpleentity.OgmTestBase;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.junit.Assert;

/**
 * ManyToManyTest
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 29. 오후 9:10
 */
@Slf4j
public class ManyToManyTest extends OgmTestBase {

    public void testMappings() {
        Session session = openSession();
        Assert.assertNotNull(session);
        session.close();
    }

    public void testManyToMany() {
    }

    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class<?>[]{
                AccountOwner.class,
                BankAccount.class
        };
    }
}
