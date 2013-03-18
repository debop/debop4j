package com.kt.vital.domain;

import com.kt.vital.VitalContext;
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
 * com.kt.vital.domain.VitalDomainTestBase
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 18 오후 3:51
 */
public class VitalDomainTestBase extends DatabaseTestFixtureBase {

    @BeforeClass
    public static void beforeClass() {
        initHibernateAndSpring(VitalConfiguration.class);
        getCurrentContext().createUnitOfWork();

        // 여기에 VitalContext.Current 에 값을 넣는다.
        VitalContext.Current.setDepartmentCode(SampleData.getDepartmentCode());
        VitalContext.Current.setUsername(SampleData.getUserName());
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
