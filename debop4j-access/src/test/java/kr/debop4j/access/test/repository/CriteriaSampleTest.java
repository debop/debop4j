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

package kr.debop4j.access.test.repository;

import com.mysema.query.jpa.hibernate.HibernateUpdateClause;
import kr.debop4j.access.model.organization.Department;
import kr.debop4j.access.model.organization.DepartmentMember;
import kr.debop4j.access.model.organization.Employee;
import kr.debop4j.access.model.organization.QEmployee;
import kr.debop4j.data.hibernate.repository.IHibernateDao;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.*;
import org.hibernate.sql.JoinType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * kr.debop4j.access.test.repository.CriteriaSampleTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 6. 15. 오후 6:26
 */
@Slf4j
@Transactional
public class CriteriaSampleTest extends RepositoryTestBase {

    @Autowired
    IHibernateDao hibernateDao;

    @Test
    @Transactional( readOnly = true )
    public void simpleWhere() {

        DetachedCriteria dc = DetachedCriteria.forClass(Employee.class);
        dc.add(Restrictions.eq("name", "Smith"));

        Employee employee = hibernateDao.findUnique(Employee.class, dc);
        log.info("Employee=[{}]", employee);

        dc.add(Restrictions.gt("age", 40));

        employee = hibernateDao.findUnique(Employee.class, dc);
        log.info("Employee=[{}]", employee);
    }

    @Test
    @Transactional( readOnly = true )
    public void joinSample() {

        DetachedCriteria dc = DetachedCriteria.forClass(Employee.class);
        dc.add(Restrictions.eq("name", "Smith"))
                .createAlias("company", "c", JoinType.INNER_JOIN)
                .add(Restrictions.eq("c.code", "KTH"));

        Employee employee = hibernateDao.findFirst(Employee.class, dc);
        log.info("Employee=[{}]", employee);
    }

    @Test
    @Transactional( readOnly = true )
    public void groupingTest() {

        DetachedCriteria dc = DetachedCriteria.forClass(Employee.class);

        dc.createAlias("company", "c")
                .createAlias("empGrade", "eg")
                .setProjection(Projections.projectionList()
                                       .add(Projections.groupProperty("c.code"))
                                       .add(Projections.groupProperty("eg.code"))
                                       .add(Projections.rowCount()));

        List loaded = dc.getExecutableCriteria(hibernateDao.getSession()).list();
        log.info("Group by = [{}]", loaded);
    }

    @Test
    @Transactional
    public void deleteTest() {

        DetachedCriteria dc = DetachedCriteria.forClass(Employee.class);
        dc.createAlias("empGrade", "eg")
                .add(Restrictions.eq("eg.code", "GRD001"));

        hibernateDao.deleteAll(Employee.class, dc);
    }

    @Test
    public void updateTest() {
        QEmployee employee = QEmployee.employee;

        // rename customers name Bob to Bobby
        new HibernateUpdateClause(getCurrentSession(), employee)
                .set(employee.name, "Bobby")
                .where(employee.name.eq("Bob"))
                .execute();
    }

    @Test
    @Transactional( readOnly = true )
    public void subqueriesTest() {

        DetachedCriteria memberSizeDc = DetachedCriteria.forClass(DepartmentMember.class);
        memberSizeDc.setProjection(Projections.projectionList()
                                           .add(Projections.groupProperty("department"))
                                           .add(Projections.rowCount(), "count"))
                .addOrder(Order.desc("count"));

        Object[] members = (Object[]) memberSizeDc.getExecutableCriteria(hibernateDao.getSession()).setMaxResults(1).uniqueResult();

        if (members != null) {
            Long departmentId = (Long) members[0];
            DetachedCriteria dc = DetachedCriteria.forClass(Department.class);
            dc.add(Restrictions.eq("id", departmentId));
            dc.getExecutableCriteria(UnitOfWorks.getCurrentSession()).list();
        }
    }

    @Test
    @Transactional( readOnly = true )
    public void subqueriesTest2() {
        DetachedCriteria memberSizeDc = DetachedCriteria.forClass(DepartmentMember.class);
        memberSizeDc.setProjection(Projections.projectionList()
                                           .add(Projections.groupProperty("department"))
                                           .add(Projections.rowCount(), "count"));

        DetachedCriteria dc = DetachedCriteria.forClass(Department.class);

        dc.add(Subqueries.propertyEq("id", memberSizeDc));

    }
}
