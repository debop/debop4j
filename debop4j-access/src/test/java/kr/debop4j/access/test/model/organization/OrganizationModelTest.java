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
import kr.debop4j.data.hibernate.repository.IHibernateDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.access.test.model.organization.OrganizationModelTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 14 오전 11:27
 */
@Slf4j
public class OrganizationModelTest extends AccessTestBase {

    @Autowired
    @Qualifier("hibernateDao")
    IHibernateDao dao;

    @Override
    protected void doBefore() {
        super.doBefore();

        new OrganizationSampleDataBuilder(dao).createSampleData();
        dao.getSession().flush();
        dao.getSession().clear();
    }

    @Test
    @Transactional
    public void createCompany() {
        Company company = new Company("KTH", "케이티하이텔");
        company.addLocaleValue(Locale.KOREA,
                               new Company.CompanyLocale("케이티하이텔", "케이티 자회사입니다."));
        company.addLocaleValue(Locale.ENGLISH,
                               new Company.CompanyLocale("KTHitel", "KTHitel ~"));

        dao.saveOrUpdate(company);
        dao.flushSession();

        Company loaded = dao.get(Company.class, company.getId());
        assertThat(loaded).isEqualTo(company);
        assertThat(loaded.getLocaleMap().size()).isEqualTo(2);

        dao.delete(loaded);
        dao.flushSession();
    }
}
