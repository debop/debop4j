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
import kr.debop4j.core.tools.StringTool;
import kr.debop4j.data.hibernate.repository.impl.HibernateDao;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.DetachedCriteria;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Locale;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.access.test.repository.organization.CompanyRepositoryTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 12.
 */
@Slf4j
public class CompanyRepositoryTest extends RepositoryTestBase {

    @Getter
    @Autowired
    private CompanyRepository repository;

    private Company company;
    private Company loaded;

    public CompanyRepositoryTest() {
        HibernateDao dao = new HibernateDao();
        dao.deleteAll(Company.class);
        dao.transactionalFlush();
    }

    @Before
    public void before() {

        company = new Company(DefaultCompanyCode, "케이티하이텔");

        getRepository().saveOrUpdate(company);
        UnitOfWorks.getCurrent().transactionalFlush();
        UnitOfWorks.getCurrent().clearSession();
    }

    @After
    public void after() {
        if (loaded != null) {
            getRepository().delete(loaded);
            UnitOfWorks.getCurrent().transactionalFlush();
        }
    }

    @Test
    public void findById() {
        loaded = getRepository().get(company.getId());
        assertThat(loaded).isNotNull();
        assertThat(loaded.getCode()).isEqualTo(company.getCode());
        assertThat(loaded.getName()).isEqualTo(company.getName());
    }

    @Test
    public void findByCode() {
        loaded = getRepository().findByCode(DefaultCompanyCode);
        assertThat(loaded).isNotNull();
        assertThat(loaded.getCode()).isEqualTo(DefaultCompanyCode);
    }

    @Test
    public void getByName() {

        loaded = Iterables.getFirst(getRepository().findByName("케이"), null);
        Assert.assertNotNull(loaded);
        Assert.assertTrue(StringTool.contains(loaded.getName(), "케이"));
    }

    @Test
    public void getByCodeAndName() {
        DetachedCriteria dc = getRepository().buildCriteria(company.getCode(), company.getName(), null);
        List<Company> companys = getRepository().find(dc);
        assertThat(companys.size()).isEqualTo(1);
        assertThat(companys.get(0).getCode()).isEqualToIgnoringCase(company.getCode());
    }

    @Test
    public void localeTest() throws Exception {

        company.addLocaleValue(Locale.KOREA,
                               new Company.CompanyLocale("케이티하이텔", "케이티 자회사입니다."));

        company.addLocaleValue(Locale.ENGLISH,
                               new Company.CompanyLocale("KTHitel", "KTHitel ~"));

        getRepository().saveOrUpdate(company);
        UnitOfWorks.getCurrent().transactionalFlush();
        UnitOfWorks.getCurrent().clearSession();

        loaded = getRepository().get(company.getId());
        Assert.assertNotNull(loaded);
        Assert.assertEquals(2, loaded.getLocaleMap().size());

        for (Company.CompanyLocale companyLocale : loaded.getLocaleMap().values()) {
            CompanyRepositoryTest.log.debug("CompanyLocale=[{}]", companyLocale);
        }
    }
}
