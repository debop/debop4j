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

package kr.debop4j.access;

import com.mysema.query.jpa.hibernate.HibernateQuery;
import kr.debop4j.access.model.organization.Company;
import kr.debop4j.access.model.organization.Department;
import kr.debop4j.access.model.organization.QCompany;
import kr.debop4j.access.model.organization.QDepartment;
import kr.debop4j.access.model.product.QUser;
import kr.debop4j.access.model.product.User;
import kr.debop4j.core.Local;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import lombok.extern.slf4j.Slf4j;

import static kr.debop4j.core.Guard.shouldNotBeEmpty;

/**
 * Access Library에서 제공하는 기본 Context 입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 10.
 */
@Slf4j
public class AccessContext {

    private AccessContext() {}

    public static final String LIBRARY_NAME = "Access";
    public static final String ADMINISTRATOR = "admin";

    public static final String UPDATE_TIMESTAMP = "updateTimestamp";

    private static final String CURRENT_COMPANY_CODE_KEY = "kr.debop4j.access.current.companyCode";
    private static final String CURRENT_DEPARTMENT_CODE_KEY = "kr.debop4j.access.current.departmentCode";
    private static final String CURRENT_USERNAME_KEY = "kr.debop4j.access.current.username";


    /** 현 Thread Context 에 제공된 정보 */
    public static class Current {

        /**
         * Gets company code.
         *
         * @return the company code
         */
        public static String getCompanyCode() {
            return (String) Local.get(CURRENT_COMPANY_CODE_KEY);
        }

        /**
         * Sets company code.
         *
         * @param companyCode the company code
         */
        public static void setCompanyCode(String companyCode) {
            shouldNotBeEmpty(companyCode, "companyCode");
            Local.put(CURRENT_COMPANY_CODE_KEY, companyCode);
        }

        /**
         * Gets company.
         *
         * @return the company
         */
        public static Company getCompany() {
            HibernateQuery query = new HibernateQuery(UnitOfWorks.getCurrentSession());
            QCompany company = QCompany.company;
            return query
                    .from(company)
                    .where(company.code.eq(getCompanyCode()))
                    .singleResult(company);
//            return (Company) UnitOfWorks.getCurrentSession()
//                    .createCriteria(Company.class)
//                    .add(Restrictions.eq("code", getCompanyCode()))
//                    .uniqueResult();
        }

        /**
         * Gets department code.
         *
         * @return the department code
         */
        public static String getDepartmentCode() {
            return (String) Local.get(CURRENT_DEPARTMENT_CODE_KEY);
        }

        /**
         * Sets department code.
         *
         * @param departmentCode the department code
         */
        public static void setDepartmentCode(String departmentCode) {
            shouldNotBeEmpty(departmentCode, "departmentCode");
            Local.put(CURRENT_DEPARTMENT_CODE_KEY, departmentCode);
        }

        /**
         * Gets department.
         *
         * @return the department
         */
        public static Department getDepartment() {
            HibernateQuery query = new HibernateQuery(UnitOfWorks.getCurrentSession());
            QDepartment department = QDepartment.department;

            return query.from(department)
                    .where(department.code.eq(getDepartmentCode()))
                    .singleResult(department);

//            return (Department) UnitOfWorks.getCurrentSession()
//                    .createCriteria(Department.class)
//                    .add(Restrictions.eq("code", getDepartmentCode()))
//                    .uniqueResult();
        }

        /**
         * Gets username.
         *
         * @return the username
         */
        public static String getUsername() {
            return (String) Local.get(CURRENT_USERNAME_KEY);
        }

        /**
         * Sets username.
         *
         * @param username the username
         */
        public static void setUsername(String username) {
            shouldNotBeEmpty(username, "username");
            Local.put(CURRENT_USERNAME_KEY, username);
        }

        /**
         * Gets user.
         *
         * @return the user
         */
        public static User getUser() {
            HibernateQuery query = new HibernateQuery(UnitOfWorks.getCurrentSession());
            QUser user = QUser.user;

            return query.from(user)
                    .where(user.username.eq(getUsername()))
                    .singleResult(user);

//            return (User) UnitOfWorks.getCurrentSession()
//                    .createCriteria(User.class)
//                    .add(Restrictions.eq("username", getUsername()))
//                    .uniqueResult();
        }
    }
}
