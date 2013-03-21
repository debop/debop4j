package kr.debop4j.data.hibernate.forTesting;

import kr.debop4j.core.spring.Springs;
import kr.debop4j.data.hibernate.unitofwork.IUnitOfWorkFactory;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import lombok.extern.slf4j.Slf4j;

/**
 * Hibernate를 이용한 Domain 테스트를 위한 UnitOfWork Context
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 19.
 */
@Slf4j
public class UnitOfWorkTestContext extends UnitOfWorkTestContextBase {

    public UnitOfWorkTestContext(Class dbConfigurationClass) {
        super(dbConfigurationClass);
    }

    @Override
    public void initializeContainerAndUnitOfWorkFactory() {
        if (log.isDebugEnabled())
            log.debug("Spring ApplicationContext 와 UnitOfWorkFactory를 초기화합니다.");

        if (UnitOfWorks.isStarted()) {
            UnitOfWorks.stop();
            UnitOfWorks.closeUnitOfWorkFactory();
        }

        // 이건 필요 없다.
        // resetSpringContext();

        Springs.init(getSpringContext());
        IUnitOfWorkFactory factory = Springs.getBean(IUnitOfWorkFactory.class);
    }

    protected void resetSpringContext() {
        if (springContext != null) {
            Springs.reset();
            springContext = null;
        } else {
            Springs.reset(springContext);
        }
    }
}
