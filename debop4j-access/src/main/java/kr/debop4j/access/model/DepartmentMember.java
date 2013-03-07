package kr.debop4j.access.model;

import com.google.common.base.Objects;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.IUpdateTimestampedEntity;
import kr.debop4j.data.model.LongAnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Date;

/**
 * 한 부서의 구성원 정보
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 5 오후 4:33
 */
@Entity
@Table(name = "DepartmentMember")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class DepartmentMember extends LongAnnotatedEntityBase implements IUpdateTimestampedEntity {

    protected DepartmentMember() {}

    public DepartmentMember(Department department, Employee employee) {
        Guard.shouldNotBeNull(department, "department");
        Guard.shouldNotBeNull(employee, "employee");
        Guard.shouldBe(department.getCompany() == employee.getCompany(), "같은 회사여야 합니다.");

        this.department = department;
        this.employee = employee;
    }

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

    @Column(name = "IsActive")
    private Boolean active;

    /**
     * 직책
     */
    @Column(name = "EmployeeTitle", length = 128)
    private String employeeTitle;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTimestamp;

    @Override
    public void updateUpdateTimestamp() {
        updateTimestamp = new Date();
    }

    @Override
    public int hashCode() {
        if (isPersisted())
            return super.hashCode();
        return HashTool.compute(department, employee);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("departmentId", department.getId())
                .add("employeeId", employee.getId())
                .add("active", active)
                .add("startTime", startTime)
                .add("endTime", endTime);
    }


}
