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

package kr.debop4j.access.test;

import kr.debop4j.access.AccessContext;
import kr.debop4j.data.hibernate.forTesting.DatabaseTestFixtureBase;
import kr.debop4j.data.hibernate.repository.IHibernateRepository;
import kr.debop4j.data.hibernate.repository.IHibernateRepositoryFactory;
import kr.debop4j.data.model.IStatefulEntity;
import lombok.Getter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Hibernate를 이용한 UnitOfWork로 테스트하기 위한 클래스
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 2.
 */
@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( classes = { AppConfig.class } )
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

    @Getter
    @Autowired
    private IHibernateRepositoryFactory factory;// = Springs.getBean(IHibernateRepositoryFactory.class);

    public <T extends IStatefulEntity> IHibernateRepository<T> getRepository(Class<T> entityClass) {
        return getFactory().getOrCreateHibernateRepository(entityClass);
    }
}
