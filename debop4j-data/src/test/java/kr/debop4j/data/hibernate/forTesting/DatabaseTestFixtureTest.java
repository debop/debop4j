package kr.debop4j.data.hibernate.forTesting;

import kr.debop4j.core.spring.Springs;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * kr.debop4j.data.hibernate.forTesting.DatabaseTestFixtureTest
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
        Springs.reset();
        closeUnitOfWorkTestContexts();
    }

    @Test
    public void canCreateUnitOfWorkContextForHSql() {
        verifyCanCreateUnitOfWorkContextFor(HSqlDbConfig.class);
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
