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
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 10.
 */
@Slf4j
public class AccessContext {

    private AccessContext() {}

    private static final String LibraryName = "Access";
    private static final String Administrator = "admin";

    private static final String CurrentCompanyCodeKey = "kr.debop4j.access.current.companyCode.key";
    private static final String CurrentDepartmentCodeKey = "kr.debop4j.access.current.departmentCode.key";
    private static final String CurrentUsernameKey = "kr.debop4j.access.current.username.key";


    public static class Current {

        public static String getCompanyCode() {
            return (String) Local.get(CurrentCompanyCodeKey);
        }

        public static void setCompanyCode(String companyCode) {
            Guard.shouldNotBeEmpty(companyCode, "companyCode");
            Local.put(CurrentCompanyCodeKey, companyCode);
        }

        public static Company getCompany() {
            return (Company) UnitOfWorks.getCurrentSession()
                    .createCriteria(Company.class)
                    .add(Restrictions.eq("code", getCompanyCode()))
                    .uniqueResult();
        }

        public static String getDepartmentCode() {
            return (String) Local.get(CurrentDepartmentCodeKey);
        }

        public static void setDepartmentCode(String departmentCode) {
            Guard.shouldNotBeEmpty(departmentCode, "departmentCode");
            Local.put(CurrentDepartmentCodeKey, departmentCode);
        }

        public static Department getDepartment() {
            return (Department) UnitOfWorks.getCurrentSession()
                    .createCriteria(Department.class)
                    .add(Restrictions.eq("code", getDepartmentCode()))
                    .uniqueResult();
        }

        public static String getUsername() {
            return (String) Local.get(CurrentUsernameKey);
        }

        public static void setUsername(String username) {
            Guard.shouldNotBeEmpty(username, "username");
            Local.put(CurrentUsernameKey, username);
        }

        public static User getUser() {
            return (User) UnitOfWorks.getCurrentSession()
                    .createCriteria(User.class)
                    .add(Restrictions.eq("username", getUsername()))
                    .uniqueResult();
        }
    }
}
