package kr.debop4j.access.test;

import kr.debop4j.access.AccessContext;
import kr.debop4j.core.spring.Springs;
import kr.debop4j.data.hibernate.forTesting.DatabaseTestFixtureBase;
import kr.debop4j.data.hibernate.repository.IHibernateRepository;
import kr.debop4j.data.hibernate.repository.IHibernateRepositoryFactory;
import kr.debop4j.data.model.IStatefulEntity;
import lombok.Getter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * Hibernate를 이용한 UnitOfWork로 테스트하기 위한 클래스
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 2.
 */
public class AccessTestBase extends DatabaseTestFixtureBase {

    @BeforeClass
    public static void beforeClass() {
        initHibernateAndSpring(AppConfig.class);
        getCurrentContext().createUnitOfWork();

        // 여기에 AccessContext.Current 에 값을 넣는다.
        AccessContext.Current.setCompanyCode(SampleData.getCompanyCode());
        AccessContext.Current.setDepartmentCode(SampleData.getDepartmentCode());
        AccessContext.Current.setUsername(SampleData.getUserName());
    }

    @AfterClass
    public static void afterClass() {
        closeUnitOfWorkTestContexts();
    }

    @Before
    public void before() {
        doBefore();
    }

    @After
    public void after() {
        doAfter();
    }

    protected void doBefore() {}

    protected void doAfter() {}

    @Getter(lazy = true)
    private static final IHibernateRepositoryFactory factory = Springs.getBean(IHibernateRepositoryFactory.class);

    public <T extends IStatefulEntity> IHibernateRepository<T> getRepository(Class<T> entityClass) {
        return getFactory().getOrCreateHibernateRepository(entityClass);
    }
}
