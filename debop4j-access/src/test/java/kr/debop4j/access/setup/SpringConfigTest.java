package kr.debop4j.access.setup;

import kr.debop4j.access.AppConfig;
import kr.debop4j.access.repository.organization.CompanyRepository;
import kr.debop4j.core.spring.Springs;
import kr.debop4j.data.hibernate.forTesting.DatabaseTestFixtureBase;
import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * kr.debop4j.access.setup.SpringConfigTest
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 12.
 */
@Slf4j
public class SpringConfigTest extends DatabaseTestFixtureBase {

    @BeforeClass
    public static void beforeClass() {
        initHibernateAndSpring(AppConfig.class);
        getCurrentContext().createUnitOfWork();
    }

    @AfterClass
    public static void afterClass() {
        closeUnitOfWorkTestContexts();
    }

    @Test
    public void repositoryExists() {
        CompanyRepository repository = Springs.getBean(CompanyRepository.class);
        Assert.assertNotNull(repository);
    }
}
