/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.debop4j.data.hibernate.forTesting;

import kr.debop4j.core.spring.Springs;
import kr.debop4j.data.hibernate.unitofwork.IUnitOfWorkFactory;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import lombok.extern.slf4j.Slf4j;

/**
 * Hibernate를 이용한 Domain 테스트를 위한 UnitOfWork Context
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 2. 19.
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
