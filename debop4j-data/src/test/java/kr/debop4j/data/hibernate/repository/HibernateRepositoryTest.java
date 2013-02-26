package kr.debop4j.data.hibernate.repository;

import kr.debop4j.core.spring.Springs;
import kr.debop4j.data.AppConfig;
import kr.debop4j.data.hibernate.unitofwork.IUnitOfWork;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import kr.debop4j.data.mapping.model.annotated.JpaUser;
import kr.debop4j.data.mapping.model.hbm.Category;
import kr.debop4j.data.mapping.model.hbm.Event;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.*;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * kr.nsoft.data.hibernate.dao.HibernateRepositoryTest
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 11. 26.
 */
@Slf4j
public class HibernateRepositoryTest {

    HibernateRepositoryFactory hibernateRepositoryfactory;
    HibernateTransactionManager transactionManager;
    IUnitOfWork unitOfWork;

    @BeforeClass
    public static void beforeClass() {
        if (Springs.isNotInitialized())
            Springs.initByAnnotatedClasses(AppConfig.class);
    }

    @Before
    public void before() {
        hibernateRepositoryfactory = Springs.getBean(HibernateRepositoryFactory.class);
        transactionManager = Springs.getBean(HibernateTransactionManager.class);

        UnitOfWorks.start();
    }

    @After
    public void after() throws Exception {
        if (UnitOfWorks.isStarted())
            UnitOfWorks.stop();
    }

    @AfterClass
    public static void afterClass() {
        if (Springs.isInitialized())
            Springs.reset();
    }

    @Test
    @Transactional
    public void createHibernateDao() {

        Assert.assertNotNull(hibernateRepositoryfactory);

        //TransactionStatus txstatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            IHibernateRepository<JpaUser> jpaUserDao = hibernateRepositoryfactory.getOrCreateHibernateRepository(JpaUser.class);
            List<JpaUser> users = jpaUserDao.getAll();

            Assert.assertEquals(0, users.size());

            //transactionManager.commit(txstatus);
        } catch (Exception e) {
            //transactionManager.rollback(txstatus);
            HibernateRepositoryTest.log.error("예외가 발생했습니다.", e);
            Assert.fail();
        }
    }

    @Test
    public void createCategoryHiberateDao() {
        IHibernateRepository<Category> categoryDao = hibernateRepositoryfactory.getOrCreateHibernateRepository(Category.class);
        List<Category> categories = categoryDao.getAll();
        Assert.assertEquals(0, categories.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void loadSessionFactory() {
        SessionFactory sessionFactory = Springs.getBean(SessionFactory.class);
        Assert.assertNotNull(sessionFactory);

        Session session = sessionFactory.openSession();
        Assert.assertNotNull(session);
        List<Event> events = session.createCriteria(Event.class).list();

        session.close();
    }
}
