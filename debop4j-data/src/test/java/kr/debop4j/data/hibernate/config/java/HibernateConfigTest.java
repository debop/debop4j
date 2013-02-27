package kr.debop4j.data.hibernate.config.java;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * kr.debop4j.data.hibernate.config.java.HibernateConfigTest
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {HibernateConfig.class})
public class HibernateConfigTest {

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    public void retrieveAccount() {
        // 트랜잭션의 부분이 아닌 경우, 수동으로 Session을 열어야 합니다.
        Session session = sessionFactory.openSession();

        Query query = session.createQuery("from Account a where a.id=:id").setLong("id", 1L);
        Account a = (Account) query.uniqueResult();

        session.close();
        Assert.assertEquals(a.getCashBalance(), 500.0, 1.0e-8);
    }

    @Test
    @Transactional
    public void updateAccount() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Account a where a.id=:id").setLong("id", 1L);
        Account a = (Account) query.uniqueResult();
        a.setName("foo");
    }
}
