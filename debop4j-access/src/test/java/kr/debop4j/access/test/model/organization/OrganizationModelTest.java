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

package kr.debop4j.access.test.model.organization;

import kr.debop4j.access.model.organization.Company;
import kr.debop4j.access.test.AccessTestBase;
import kr.debop4j.data.hibernate.repository.IHibernateRepository;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Locale;

/**
 * kr.debop4j.access.test.model.organization.OrganizationModelTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 14 오전 11:27
 */
@Slf4j
public class OrganizationModelTest extends AccessTestBase {

    @BeforeClass
    public static void beforeClass() {
        AccessTestBase.beforeClass();
        new OrganizationSampleDataBuilder().createSampleData();
    }

    @AfterClass
    public static void afterClass() {
        AccessTestBase.afterClass();
    }

    @Override
    protected void doBefore() {
        super.doBefore();
        UnitOfWorks.getCurrentSession().clear();
    }

    @Test
    public void createCompany() {
        IHibernateRepository<Company> repository = getRepository(Company.class);

        Company company = new Company("KTH", "케이티하이텔");
        company.addLocaleValue(Locale.KOREA,
                               new Company.CompanyLocale("케이티하이텔", "케이티 자회사입니다."));
        company.addLocaleValue(Locale.ENGLISH,
                               new Company.CompanyLocale("KTHitel", "KTHitel ~"));

        repository.saveOrUpdate(company);
        UnitOfWorks.getCurrent().transactionalFlush();
        UnitOfWorks.getCurrent().clearSession();

        Company loaded = repository.get(company.getId());
        Assert.assertEquals(company, loaded);

        Assert.assertEquals(2, loaded.getLocaleMap().size());

        repository.delete(loaded);
        UnitOfWorks.getCurrent().transactionalFlush();
    }
}
