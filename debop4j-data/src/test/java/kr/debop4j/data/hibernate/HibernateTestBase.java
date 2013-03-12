package kr.debop4j.data.hibernate;

import kr.debop4j.data.AppConfig;
import kr.debop4j.data.hibernate.forTesting.DatabaseTestFixtureBase;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * kr.nsoft.data.hibernate.HibernateTestBase
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 11. 26.
 */
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
