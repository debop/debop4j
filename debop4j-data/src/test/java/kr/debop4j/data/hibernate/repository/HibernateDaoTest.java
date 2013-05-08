package kr.debop4j.data.hibernate.repository;

import kr.debop4j.core.spring.Springs;
import kr.debop4j.data.hibernate.HibernateTestBase;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import kr.debop4j.data.mapping.model.annotated.JpaUser;
import kr.debop4j.data.mapping.model.hbm.Category;
import kr.debop4j.data.mapping.model.hbm.Event;
import lombok.extern.slf4j.Slf4j;
import org.fest.assertions.Assertions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * kr.debop4j.data.hibernate.repository.HibernateDaoTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 15. 오후 2:08
 */
@Slf4j
public class HibernateDaoTest extends HibernateTestBase {

    @Autowired
    IHibernateDao hibernateDao;

    @Autowired
    HibernateTransactionManager transactionManager;

    @Before
    public void before() {
        UnitOfWorks.start();
    }

    @After
    public void after() throws Exception {
        if (UnitOfWorks.isStarted())
            UnitOfWorks.stop();
    }

    @Test
    @Transactional
    public void createHibernateRepository() {

        Assert.assertNotNull(hibernateDao);

        //TransactionStatus txstatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            List<JpaUser> users = hibernateDao.findAll(JpaUser.class);

            Assert.assertEquals(0, users.size());

            //transactionManager.commit(txstatus);
        } catch (Exception e) {
            //transactionManager.rollback(txstatus);
            log.error("예외가 발생했습니다.", e);
            Assert.fail();
        }
    }

    @Test
    public void createCategoryHiberateRepository() {

        List<Category> categories = hibernateDao.findAll(Category.class);
        Assert.assertEquals(0, categories.size());
    }

    @Test
    @SuppressWarnings( "unchecked" )
    public void loadSessionFactory() {
        SessionFactory sessionFactory = Springs.getBean(SessionFactory.class);
        Assert.assertNotNull(sessionFactory);

        Session session = sessionFactory.openSession();
        Assert.assertNotNull(session);
        List<Event> events = session.createCriteria(Event.class).list();

        session.close();
    }

    @Test
    public void findAll_Test() throws Exception {
        List<Category> categories = hibernateDao.findAll(Category.class);
        Assertions.assertThat(categories).isNotNull();
        Assertions.assertThat(categories.size()).isEqualTo(0);
    }
}
