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

import kr.debop4j.access.AccessContext;
import kr.debop4j.access.model.organization.Company;
import kr.debop4j.access.test.SampleData;
import kr.debop4j.access.test.SampleDataBuilder;
import kr.debop4j.data.hibernate.repository.IHibernateDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

/**
 * kr.debop4j.access.test.model.organization.OrganizationSampleDataBuilder
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 14 오전 10:38
 */
@Slf4j
@Transactional
public class OrganizationSampleDataBuilder extends SampleDataBuilder {

    private final IHibernateDao dao;

    public OrganizationSampleDataBuilder(IHibernateDao dao) {
        this.dao = dao;
    }

    @Override
    public void createSampleData() {
        createCompany();
    }

    @SuppressWarnings( "unchecked" )
    private void createCompany() {
        Company company = new Company(AccessContext.Current.getCompanyCode());
        company.setName(company.getCode() + " Name");
        company.setDescription("테스트용 기본 클래스");
        company.setExAttr("확장 속성 정보입니다.");

        company.addLocaleValue(Locale.ENGLISH, new Company.CompanyLocale("DefaultCompany", "Default Company for Testing"));
        company.addLocaleValue(Locale.KOREAN, new Company.CompanyLocale("기본 회사", "테스트를 위한 기본 회사"));

        dao.saveOrUpdate(company);

        for (String code : SampleData.getCompanyCodes()) {
            company = new Company(code);
            company.setName(company.getCode() + " Name");
            company.setDescription("테스트용 기본 클래스");
            company.setExAttr("확장 속성 정보입니다.");
            company.setActive(true);
            dao.saveOrUpdate(company);
        }
    }
}
