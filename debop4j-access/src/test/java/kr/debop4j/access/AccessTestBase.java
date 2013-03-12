package kr.debop4j.access;

import kr.debop4j.data.hibernate.forTesting.DatabaseTestFixtureBase;
import org.junit.AfterClass;
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
        AccessContext.Current.setCompanyCode("KTH");
        AccessContext.Current.setDepartmentCode("Platform");
        AccessContext.Current.setUsername("debop");
    }

    @AfterClass
    public static void afterClass() {
        closeUnitOfWorkTestContexts();
    }
}
