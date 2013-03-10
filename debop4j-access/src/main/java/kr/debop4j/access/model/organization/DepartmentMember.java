package kr.debop4j.access.model.organization;

import com.google.common.base.Objects;
import kr.debop4j.access.model.AccessEntityBase;
import kr.debop4j.access.model.common.EmployeeTitle;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * 한 부서의 구성원 정보 (직원의 겸직이 가능하므로, 부서-직원은 many-to-many 관계를 가집입니다)
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 5 오후 4:33
 */
@Entity
@Table(name = "DepartmentMember")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class DepartmentMember extends AccessEntityBase {

    private static final long serialVersionUID = 6458394469111394831L;

    protected DepartmentMember() {}

    public DepartmentMember(Department department, Employee employee) {
        Guard.shouldNotBeNull(department, "department");
        Guard.shouldNotBeNull(employee, "employee");
        Guard.shouldBe(department.getCompany() == employee.getCompany(), "같은 회사여야 합니다.");

        this.department = department;
        this.employee = employee;
    }

    @Id
    @GeneratedValue
    @Column(name = "DeptMemberId")
    private Long id;

    /**
     * 소속 부서
     */
    @ManyToOne
    @JoinColumn(name = "DepartmentId", nullable = false)
    @Index(name = "ix_department_member")
    private Department department;

    /**
     * 소속 직원
     */
    @ManyToOne
    @JoinColumn(name = "EmployeeId", nullable = false)
    @Index(name = "ix_department_member")
    private Employee employee;

    /**
     * 소속 시작일
     */
    @Column(name = "StartTime")
    private DateTime startTime;

    /**
     * 소속 종료일
     */
    @Column(name = "EndTime")
    private DateTime endTime;

    @Column(name = "Active")
    private Boolean active;

    @Column(name = "ExAttr", length = 4000)
    private String exAttr;

    /**
     * 직책
     */
    @ManyToOne
    @JoinColumn(name = "EmpTitleId")
    private EmployeeTitle empTitle;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(department, employee);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("departmentId", department.getId())
                .add("employeeId", employee.getId())
                .add("employeeTitle", empTitle)
                .add("active", active)
                .add("startTime", startTime)
                .add("endTime", endTime);
    }
}
