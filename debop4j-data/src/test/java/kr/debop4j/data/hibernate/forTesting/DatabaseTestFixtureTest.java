package kr.debop4j.data.hibernate.forTesting;

import kr.debop4j.core.spring.Springs;
import kr.debop4j.data.hibernate.forTesting.configs.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * 다양한 Database에 대해 hibernate Domain Model을 테스트 할 수 잇도록 합니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 21.
 */
@Slf4j
public class DatabaseTestFixtureTest extends DatabaseTestFixtureBase {

    @Before
    public void before() {
    }

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
    }

    @Test
    public void canCreateUnitOfWorkContextForH2() {
        verifyCanCreateUnitOfWorkContextFor(H2Config.class);
        verifyCanCreateUseAndDisposeSession();
    }

    @Test
    public void canCreateUnitOfWorkContextForDerby() {
        verifyCanCreateUnitOfWorkContextFor(DerbyConfig.class);
        verifyCanCreateUseAndDisposeSession();
    }

    @Test
    public void canCreateUnitOfWorkContextForPostgreSql() {
        verifyCanCreateUnitOfWorkContextFor(PostgreSqlConfig.class);
        verifyCanCreateUseAndDisposeSession();
    }

    @Test
    public void canCreateUnitOfWorkContextForMySql() {
        verifyCanCreateUnitOfWorkContextFor(MySqlConfig.class);
        verifyCanCreateUseAndDisposeSession();
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

            LongEntityForTesting entity = new LongEntityForTesting("abcd");
            session.save(entity);
            session.flush();
        } finally {
            getCurrentContext().disposeSession(session);
            getCurrentContext().disposeUnitOfWork();
        }
    }
}
