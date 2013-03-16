package kr.debop4j.ogm.test;

import kr.debop4j.core.spring.Springs;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.engine.spi.SearchFactoryImplementor;
import org.hibernate.service.Service;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * Hibernate OGM 테스트 클래스
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 16.
 */
@Slf4j
public abstract class OgmTestBase {

    protected SessionFactory sessions;
    private Session session;

    @BeforeClass
    public static void beforeClass() {
        Springs.initByAnnotatedClasses(AppConfig.class);
    }

    @AfterClass
    public static void afterClass() {
    }

    @Before
    public void before() {
        UnitOfWorks.start();
    }

    @After
    public void after() {
        UnitOfWorks.stop();
    }

    protected SearchFactoryImplementor getSearchFactoryImpl() {
        FullTextSession s = Search.getFullTextSession(UnitOfWorks.getCurrentSession());
        s.close();
        SearchFactory searchFactory = s.getSearchFactory();
        return (SearchFactoryImplementor) searchFactory;
    }

    private void reportSkip(Skip skip) {

    }

    protected void reportSkip(String reason, String testDescription) {
        StringBuilder builder = new StringBuilder();
        builder.append("*** skipping test [");
        builder.append(fullTestName());
        builder.append("] - ");
        builder.append(testDescription);
        builder.append(" : ");
        builder.append(reason);
        log.warn(builder.toString());
    }

    public String fullTestName() {
        return this.getClass().getName();
    }

    protected SessionFactoryImplementor sfi() {
        return (SessionFactoryImplementor) UnitOfWorks.getCurrentSessionFactory();
    }

    protected Service getService(Class<? extends Service> serviceImpl) {
        SessionFactoryImplementor factory = sfi();
        ServiceRegistryImplementor serviceRegistry = factory.getServiceRegistry();
        return serviceRegistry.getService(serviceImpl);
    }

    protected static class Skip {
        @Getter
        private final String reason;
        @Getter
        private final String testDescription;

        public Skip(String reason, String testDescription) {
            this.reason = reason;
            this.testDescription = testDescription;
        }
    }
}
