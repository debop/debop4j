package com.kt.vital;

import com.kt.vital.domain.model.Department;
import com.kt.vital.domain.model.User;
import kr.debop4j.core.Guard;
import kr.debop4j.core.Local;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import org.hibernate.criterion.Restrictions;

/**
 * com.kt.vital.VitalContext
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 18.
 */
public class VitalContext {

    private VitalContext() {}

    private static final String CurrentDepartmentCodeKey = "kr.debop4j.access.current.departmentCode.key";
    private static final String CurrentUsernameKey = "kr.debop4j.access.current.username.key";

    public static class Current {

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
