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

package kr.debop4j.access.test.repository.organization;

import com.google.common.collect.Iterables;
import kr.debop4j.access.model.organization.Company;
import kr.debop4j.access.repository.organization.CompanyRepository;
import kr.debop4j.access.test.repository.RepositoryTestBase;
import kr.debop4j.core.spring.Springs;
import kr.debop4j.core.tools.StringTool;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

/**
 * kr.debop4j.access.test.repository.organization.CompanyRepositoryTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 12.
 */
@Slf4j
public class CompanyRepositoryTest extends RepositoryTestBase {

    @Getter(lazy = true)
    private final CompanyRepository repository = Springs.getBean(CompanyRepository.class);


    private Company createCompany() {
        Company company = new Company("KTH", "케이티하이텔");
        getRepository().saveOrUpdate(company);

        UnitOfWorks.getCurrent().transactionalFlush();
        UnitOfWorks.getCurrent().clearSession();

        return company;
    }

    @Test
    public void createTest() {
        Company company = createCompany();

        Company loaded = getRepository().get(company.getId());
        Assert.assertEquals(company.getCode(), loaded.getCode());
        Assert.assertEquals(company.getName(), loaded.getName());

        getRepository().delete(loaded);
        UnitOfWorks.getCurrent().flushSession();
    }

    @Test
    public void findByCode() {
        Company company = createCompany();
        Company loaded = getRepository().findByCode(DefaultCompanyCode);
        Assert.assertNotNull(loaded);
        Assert.assertEquals(DefaultCompanyCode, loaded.getCode());

        getRepository().delete(loaded);
        UnitOfWorks.getCurrent().flushSession();
    }

    @Test
    public void getByName() {
        Company company = createCompany();
        Company loaded = Iterables.getFirst(getRepository().findByName("케이"), null);

        Assert.assertNotNull(loaded);
        Assert.assertTrue(StringTool.contains(loaded.getName(), "케이"));

        getRepository().delete(loaded);
        UnitOfWorks.getCurrent().flushSession();
    }

    @Test
    public void localeTest() throws Exception {
        Company company = createCompany();

        company.addLocaleValue(Locale.KOREA,
                               new Company.CompanyLocale("케이티하이텔", "케이티 자회사입니다."));

        company.addLocaleValue(Locale.ENGLISH,
                               new Company.CompanyLocale("KTHitel", "KTHitel ~"));

        getRepository().saveOrUpdate(company);
        UnitOfWorks.getCurrent().transactionalFlush();
        UnitOfWorks.getCurrent().clearSession();

        Company loaded = getRepository().get(company.getId());
        Assert.assertNotNull(loaded);
        Assert.assertEquals(2, loaded.getLocaleMap().size());

        for (Company.CompanyLocale companyLocale : loaded.getLocaleMap().values()) {
            CompanyRepositoryTest.log.debug("CompanyLocale=[{}]", companyLocale);
        }

        getRepository().delete(loaded);
        UnitOfWorks.getCurrent().flushSession();
    }
}
