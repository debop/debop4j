package com.kt.vital.domain;

import com.kt.vital.domain.repository.VoCRepository;
import kr.debop4j.core.spring.Springs;
import kr.debop4j.data.hibernate.forTesting.DatabaseTestFixtureBase;
import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Vital 시스템 환경 설정 테스트
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 18.
 */
@Slf4j
public class VitalConfigurationTest extends DatabaseTestFixtureBase {

    @BeforeClass
    public static void beforeClass() {
        initHibernateAndSpring(VitalConfiguration.class);
        getCurrentContext().createUnitOfWork();
    }

    @AfterClass
    public static void afterClass() {
        closeUnitOfWorkTestContexts();
    }

    @Test
    public void repositoryExists() {
        VoCRepository repository = Springs.getBean(VoCRepository.class);
        Assert.assertNotNull(repository);
    }
}
