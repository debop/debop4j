package kr.debop4j.access;

import kr.debop4j.access.model.organization.Company;
import kr.debop4j.access.model.organization.Department;
import kr.debop4j.access.model.product.User;
import kr.debop4j.core.Guard;
import kr.debop4j.core.Local;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.Restrictions;

/**
 * Access Library에서 제공하는 기본 Context 입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 10.
 */
@Slf4j
public class AccessContext {

    private AccessContext() {}

    private static final String LIBRARY_NAME = "Access";
    private static final String ADMINISTRATOR = "admin";

    private static final String CURRENT_COMPANY_CODE_KEY = "kr.debop4j.access.current.companyCode.key";
    private static final String CURRENT_DEPARTMENT_CODE_KEY = "kr.debop4j.access.current.departmentCode.key";
    private static final String CURRENT_USERNAME_KEY = "kr.debop4j.access.current.username.key";

    /** 현 Thread Context 에 제공된 정보 */
    public static class Current {

        public static String getCompanyCode() {
            return (String) Local.get(CURRENT_COMPANY_CODE_KEY);
        }

        public static void setCompanyCode(String companyCode) {
            Guard.shouldNotBeEmpty(companyCode, "companyCode");
            Local.put(CURRENT_COMPANY_CODE_KEY, companyCode);
        }

        public static Company getCompany() {
            return (Company) UnitOfWorks.getCurrentSession()
                    .createCriteria(Company.class)
                    .add(Restrictions.eq("code", getCompanyCode()))
                    .uniqueResult();
        }

        public static String getDepartmentCode() {
            return (String) Local.get(CURRENT_DEPARTMENT_CODE_KEY);
        }

        public static void setDepartmentCode(String departmentCode) {
            Guard.shouldNotBeEmpty(departmentCode, "departmentCode");
            Local.put(CURRENT_DEPARTMENT_CODE_KEY, departmentCode);
        }

        public static Department getDepartment() {
            return (Department) UnitOfWorks.getCurrentSession()
                    .createCriteria(Department.class)
                    .add(Restrictions.eq("code", getDepartmentCode()))
                    .uniqueResult();
        }

        public static String getUsername() {
            return (String) Local.get(CURRENT_USERNAME_KEY);
        }

        public static void setUsername(String username) {
            Guard.shouldNotBeEmpty(username, "username");
            Local.put(CURRENT_USERNAME_KEY, username);
        }

        public static User getUser() {
            return (User) UnitOfWorks.getCurrentSession()
                    .createCriteria(User.class)
                    .add(Restrictions.eq("username", getUsername()))
                    .uniqueResult();
        }
    }
}
