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
import kr.debop4j.access.model.organization.CompanyCode;
import kr.debop4j.access.model.organization.CompanyCodeItem;
import kr.debop4j.core.tools.StringTool;
import kr.debop4j.data.hibernate.repository.impl.HibernateRepository;
import kr.debop4j.data.hibernate.tools.CriteriaTool;
import kr.debop4j.data.hibernate.unitofwork.IUnitOfWork;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorkNestingOptions;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for {@link CompanyCodeItem}
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 14.
 */
@Repository
@Slf4j
public class CompanyCodeItemRepository extends HibernateRepository<CompanyCodeItem> {

    public CompanyCodeItemRepository() {
        super(CompanyCodeItem.class);
    }

    public DetachedCriteria buildCriteria(Company company, String code, String name, String itemName, String itemValue) {

        if (log.isDebugEnabled())
            log.debug("Criteria를 빌드합니다. company=[{}], code=[{}], name=[{}], itemName=[{}], itemValue=[{}]",
                      company, code, name, itemName, itemValue);

        DetachedCriteria dc = DetachedCriteria.forClass(CompanyCodeItem.class);

        if (company != null) {
            dc.createAlias("code", "c");
            CriteriaTool.addEq(dc, "c.company", company);
        }
        if (StringTool.isNotEmpty(code))
            CriteriaTool.addEq(dc, "c.code", code);

        if (StringTool.isNotEmpty(name))
            CriteriaTool.addEq(dc, "c.name", name);

        if (StringTool.isNotEmpty(itemName))
            CriteriaTool.addEq(dc, "name", itemName);

        if (StringTool.isNotEmpty(itemValue))
            CriteriaTool.addEq(dc, "value", itemValue);

        return dc;
    }

    public CompanyCodeItem getOrCreate(Company company, String code, String itemName) {
        CompanyCodeItem item = findOneByName(company, code, itemName);

        if (item == null) {
            synchronized (this) {
                try (IUnitOfWork unitOfWork = UnitOfWorks.start(UnitOfWorkNestingOptions.CreateNewOrNestUnitOfWork)) {
                    CompanyCodeRepository codeRepository = new CompanyCodeRepository();
                    CompanyCode companyCode = codeRepository.getOrCreate(company, code);

                    UnitOfWorks.getCurrentSession().saveOrUpdate(new CompanyCodeItem(companyCode, itemName));
                    UnitOfWorks.getCurrentSession().saveOrUpdate(codeRepository);
                    UnitOfWorks.getCurrent().transactionalFlush();
                }
            }
            if (log.isDebugEnabled())
                log.debug("새로운 CompanyCodeItem을 생성했습니다.");

            item = findOneByName(company, code, itemName);
        }
        return item;
    }

    public CompanyCodeItem findOneByName(Company company, String code, String itemName) {
        DetachedCriteria dc = buildCriteria(company, code, null, itemName, null);

        return findOne(dc);
    }

    public List<CompanyCodeItem> findByCompany(Company company) {
        return find(buildCriteria(company, null, null, null, null));
    }
}
