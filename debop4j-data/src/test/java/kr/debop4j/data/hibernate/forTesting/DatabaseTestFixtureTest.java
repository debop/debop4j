package kr.debop4j.data.hibernate.forTesting;

import kr.debop4j.core.spring.Springs;
import kr.debop4j.data.hibernate.forTesting.configs.*;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.junit.*;

/**
 * 다양한 Database에 대해 hibernate Domain Model을 테스트 할 수 잇도록 합니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 2. 21.
 */
@Slf4j
public class DatabaseTestFixtureTest extends DatabaseTestFixtureBase {

    @Before
    public void before() { }

    @After
    public void after() {
        // 여러 DB 를 교체하여 테스트하므로 꼭 reset 작업을 해주어야 합니다.
        Springs.reset();
        closeUnitOfWorkTestContexts();
    }

    @Test
    public void canCreateUnitOfWorkContextForHSql() {
        verifyCanCreateUnitOfWorkContextFor(HSqlConfig.class);
        verifyCanCreateUseAndDisposeSession();
        verifyCanCreateUseAndDisposeUnitOfWork();
    }

    @Test
    public void canCreateUnitOfWorkContextForH2() {
        verifyCanCreateUnitOfWorkContextFor(H2Config.class);
        verifyCanCreateUseAndDisposeSession();
        verifyCanCreateUseAndDisposeUnitOfWork();
    }

//    @Test
//    public void canCreateUnitOfWorkContextForDerby() {
//        verifyCanCreateUnitOfWorkContextFor(DerbyConfig.class);
//        verifyCanCreateUseAndDisposeSession();
//        verifyCanCreateUseAndDisposeUnitOfWork();
//    }

    @Test
    @Ignore("MySQL을 설치하세요")
    public void canCreateUnitOfWorkContextForMySql() {
        verifyCanCreateUnitOfWorkContextFor(MySqlConfig.class);
        verifyCanCreateUseAndDisposeSession();
        verifyCanCreateUseAndDisposeUnitOfWork();
    }

    @Test
    public void canCreateUnitOfWorkContextForPostgreSql() {
        verifyCanCreateUnitOfWorkContextFor(PostgreSqlConfig.class);
        verifyCanCreateUseAndDisposeSession();
        verifyCanCreateUseAndDisposeUnitOfWork();
    }

    @Test
    @Ignore("PgPool-II 를 설치해야만 테스트가 가능합니다. - Connection Pool 로만 써도 상당한 성능 향상이 있습니다.")
    public void canCreateUnitOfWorkContextForPgPool() {
        verifyCanCreateUnitOfWorkContextFor(PgPoolConfig.class);
        verifyCanCreateUseAndDisposeSession();
        verifyCanCreateUseAndDisposeUnitOfWork();
    }

    @Test
    @Ignore("pgBouncer를 설치해야만 테스트가 가능합니다. - PostgreSql의 Connection Pool 기능입니다.")
    public void canCreateUnitOfWorkContextForPgBouncer() {
        verifyCanCreateUnitOfWorkContextFor(PgBouncerConfig.class);
        verifyCanCreateUseAndDisposeSession();
        verifyCanCreateUseAndDisposeUnitOfWork();
    }


    @Test
    public void eachUnitOfWrokContextConfigurationWillBeCreatedOnlyOnce() {
        initHibernateAndSpring(HSqlConfig.class);
        initHibernateAndSpring(HSqlConfig.class);

        Assert.assertEquals(1, getContexts().size());
    }

    @Test
    public void newUnitOfWorkContextCreatedForDifferentWindsorConfigFiles() {
        initHibernateAndSpring(HSqlConfig.class);
        initHibernateAndSpring(HSqlConfig2.class);

        Assert.assertEquals(2, getContexts().size());
    }

    protected void verifyCanCreateUnitOfWorkContextFor(Class dbConfigClass) {
        int nextContextPos = getContexts().size();

        initHibernateAndSpring(dbConfigClass);

        UnitOfWorkTestContextBase context = getContexts().get(nextContextPos);
        Assert.assertNotNull(context);
        Assert.assertEquals(getCurrentContext(), context);
    }

    protected void verifyCanCreateUseAndDisposeSession() {
        Session session = null;
        try {
            getCurrentContext().createUnitOfWork();
            session = getCurrentContext().createSession();
            Assert.assertNotNull(session);

            session.save(new LongEntityForTesting());
            session.flush();
        } finally {
            getCurrentContext().disposeSession(session);
            getCurrentContext().disposeUnitOfWork();
        }
    }

    protected void verifyCanCreateUseAndDisposeUnitOfWork() {
        try {
            getCurrentContext().createUnitOfWork();
            log.debug("Session=[{}]", UnitOfWorks.getCurrentSession());
            UnitOfWorks.getCurrentSession().save(new LongEntityForTesting());
            UnitOfWorks.getCurrentSession().flush();
        } finally {
            getCurrentContext().disposeUnitOfWork();
        }
    }

    protected void verifyCanCreateUseAndDiposeNestedUnitOfWork() {
        Assert.assertEquals("level before starting UnitOfWork = -1",
                            -1,
                            getCurrentContext().getUnitOfWorkNestingLevel());

        getCurrentContext().createUnitOfWork();
        Assert.assertEquals("level before starting UnitOfWork = 0",
                            0,
                            getCurrentContext().getUnitOfWorkNestingLevel());

        getCurrentContext().createNestedUnitOfWork();
        Assert.assertEquals("level after starting UnitOfWork = 1",
                            1,
                            getCurrentContext().getUnitOfWorkNestingLevel());

        // in nested unit of work
        UnitOfWorks.getCurrentSession().save(new LongEntityForTesting());
        UnitOfWorks.getCurrentSession().flush();
        getCurrentContext().disposeUnitOfWork();

        // in original unit of work
        UnitOfWorks.getCurrentSession().save(new LongEntityForTesting());
        UnitOfWorks.getCurrentSession().flush();
        getCurrentContext().disposeUnitOfWork();
    }
}
