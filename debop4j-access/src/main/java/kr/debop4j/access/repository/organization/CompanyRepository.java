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

package kr.debop4j.access.repository.organization;

import kr.debop4j.access.model.organization.Company;
import kr.debop4j.core.tools.StringTool;
import kr.debop4j.data.hibernate.HibernateParameter;
import kr.debop4j.data.hibernate.repository.impl.HibernateDao;
import kr.debop4j.data.hibernate.tools.CriteriaTool;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * {@link Company} 에 대한 Repository 입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 1.
 */
@Repository
@Qualifier( "companyRepository" )
@Transactional
@Slf4j
public class CompanyRepository extends HibernateDao implements ICompanyRepository {

    public CompanyRepository() { }

    @Override
    public DetachedCriteria buildCriteria(String code, String name, Boolean active) {

        DetachedCriteria dc = DetachedCriteria.forClass(Company.class);

        if (StringTool.isNotEmpty(code))
            dc = CriteriaTool.addEq(dc, "code", code);

        if (StringTool.isNotEmpty(name))
            dc = CriteriaTool.addEq(dc, "name", name);

        if (active != null)
            dc = CriteriaTool.addEq(dc, "active", active);

        return dc;
    }

    @Override
    @Transactional( readOnly = true )
    public Company findByCode(String code) {
        return findUniqueByNamedQuery(Company.class, "Company.findByCode", new HibernateParameter("code", code));
    }

    @Override
    @Transactional( readOnly = true )
    public List<Company> findByName(String name) {
        return findByNamedQuery(Company.class, "Company.findByName", new HibernateParameter("name", name + '%'));
    }

    @Override
    @Transactional( readOnly = true )
    public List<Company> findAllByActive(boolean active) {
        DetachedCriteria dc = DetachedCriteria.forClass(Company.class);
        return find(Company.class, dc.add(Restrictions.eq("active", active)));
    }
}
