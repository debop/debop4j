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

import com.mysema.query.Tuple;
import com.mysema.query.jpa.hibernate.HibernateDeleteClause;
import com.mysema.query.jpa.hibernate.HibernateQuery;
import com.mysema.query.jpa.hibernate.HibernateSubQuery;
import com.mysema.query.jpa.hibernate.HibernateUpdateClause;
import kr.debop4j.access.model.organization.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.mysema.query.group.GroupBy.groupBy;
import static com.mysema.query.group.GroupBy.list;

/**
 * kr.debop4j.access.test.repository.QuerydslSampleTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 6. 15. 오전 9:44
 */
@Slf4j
@Transactional
public class QuerydslSampleTest extends RepositoryTestBase {

    @Autowired
    SessionFactory sessionFactory;

    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Test
    @Transactional(readOnly = true)
    public void simpleWhere() {
        HibernateQuery query = new HibernateQuery(getCurrentSession());
        QEmployee employee = QEmployee.employee;
        Employee loaded = query.from(employee)
                .where(employee.name.eq("Smith"))
                .uniqueResult(employee);

        log.info("Employee=[{}]", loaded);
    }

    @Test
    @Transactional(readOnly = true)
    public void joinWhere() {
        HibernateQuery query = new HibernateQuery(getCurrentSession());
        QEmployee employee = QEmployee.employee;
        QCompany company = QCompany.company;
        Employee loaded = query.from(employee)
                .innerJoin(employee.company, company)
                .where(employee.name.eq("Smith").and(company.code.eq("KTH")))
                .uniqueResult(employee);

        log.info("Employee=[{}]", loaded);
    }

    @Test
    @Transactional(readOnly = true)
    public void groupingTest() {
        HibernateQuery query = new HibernateQuery(getCurrentSession());
        QEmployee employee = QEmployee.employee;
        QCompany company = QCompany.company;

        List<Tuple> loaded =
                query.from(employee)
                        .groupBy(employee.company.code, employee.empGrade.code)
                        .list(employee.company.code.as("CompanyCode"),
                              employee.empGrade.code.as("GradeCode"),
                              employee.countDistinct().as("RowCount"));

        log.info("Group by = [{}]", loaded);
    }

    @Test
    public void deleteTest() {
        QEmployee employee = QEmployee.employee;

        // delete all employee
        new HibernateDeleteClause(getCurrentSession(), employee).execute();

        // delete all employee where company code = 'KTH'
        new HibernateDeleteClause(getCurrentSession(), employee)
                .where(employee.name.like("Smith"))
                .execute();

        QCompany company = QCompany.company;

        // delete all employee where company code = 'KTH'
        new HibernateDeleteClause(getCurrentSession(), employee)
                .where(employee.company.eq(new HibernateSubQuery().from(company).where(company.code.eq("KTH")).unique(company)))
                .execute();
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
    @Transactional(readOnly = true)
    public void subQueriesTest() {
        QDepartment department = QDepartment.department;
        QDepartmentMember member = QDepartmentMember.departmentMember;
        QDepartment d = new QDepartment("d");

        HibernateQuery query = new HibernateQuery(getCurrentSession());

        QEmployee employee = QEmployee.employee;
        QEmployee e = new QEmployee("e");

        query.from(employee)
                .where(employee.age.gt(new HibernateSubQuery().from(e).unique(e.age.avg())))
                .list(employee);

        // 직원이 가장 많은 부서 (이것은 차라리 subquery 보다 이 방식이 낫다)
        List<Long> results = query.from(member).groupBy(member.department).list(member.count());

        if (results != null && results.size() > 0) {
            Long max = (Long) results.get(0);
            List<Department> bigDepartment =
                    query.from(department)
                            .where(department.members.size().eq(max.intValue()))
                            .list(department);
        }
    }

    @Test
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public void getHibernateQuery() {
        HibernateQuery query = new HibernateQuery(getCurrentSession());
        QEmployee employee = QEmployee.employee;
        org.hibernate.Query hibernateQuery =
                query.from(employee)
                        .where(employee.name.like("Smith%"))
                        .createQuery(employee);

        List<Employee> emps = hibernateQuery.list();

        for (Employee emp : emps)
            log.info("Employee=[{}]", emp);
    }

    @Test
    @Transactional(readOnly = true)
    public void transformTest() {
        QCompany company = QCompany.company;
        QEmployee employee = QEmployee.employee;
        HibernateQuery query = new HibernateQuery(getCurrentSession());

        Map<Long, List<Employee>> results =
                query.from(company)
                        .innerJoin(company.employees, employee)
                        .transform(groupBy(company.id).as(list(employee)));

    }
}
