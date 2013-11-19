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

import com.mysema.query.jpa.hibernate.HibernateQuery;
import kr.debop4j.access.model.organization.Company;
import kr.debop4j.access.model.organization.QCompany;
import kr.debop4j.access.test.repository.RepositoryTestBase;
import kr.debop4j.core.tools.StringTool;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * kr.debop4j.access.test.repository.organization.QuerydslTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 31. 오후 11:51
 */
@Slf4j
public class QuerydslTest extends RepositoryTestBase {

    @Test
    @Transactional(readOnly = true)
    public void queryCompany() {
        QCompany qCompany = QCompany.company;

        HibernateQuery query = new HibernateQuery(getCurrentSession());
        List<Company> loaded = query.from(qCompany)
                .where(qCompany.active.isTrue().and(qCompany.name.isNotEmpty()).and(qCompany.code.in("KTH", "KT")))
                .list(qCompany);

        log.debug("company={}", StringTool.listToString(loaded));
    }
}
