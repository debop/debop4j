package kr.debop4j.data.hibernate;

import kr.debop4j.data.AppConfig;
import kr.debop4j.data.hibernate.forTesting.DatabaseTestFixtureBase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * kr.nsoft.data.hibernate.HibernateTestBase
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 11. 26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
public abstract class HibernateTestBase extends DatabaseTestFixtureBase {

    @BeforeClass
    public static void beforeClass() {
        initHibernateAndSpring(AppConfig.class);
        getCurrentContext().createUnitOfWork();
    }

    @AfterClass
    public static void afterClass() {
        closeUnitOfWorkTestContexts();
    }
}
