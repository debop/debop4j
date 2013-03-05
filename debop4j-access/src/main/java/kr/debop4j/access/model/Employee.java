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

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * kr.debop4j.access.model.Employee
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 1.
 */
@Entity
@Table(name = "Employee")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Employee extends LongAnnotatedEntityBase implements ICodeBaseEntity, IUpdateTimestampedEntity {

    protected Employee() {}

    public Employee(Company company, String employeeCode) {
        this(company, employeeCode, employeeCode);
    }

    public Employee(Company company, String employeeCode, String employeeName) {
        Guard.shouldNotBeNull(company, "company");
        Guard.shouldNotBeEmpty(employeeCode, "employeeCode");

        this.company = company;
        this.code = employeeCode;
        this.name = employeeName;
    }

    @Id
    @GeneratedValue
    @Column(name = "EmployeeId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CompanyId", nullable = false)
    private Company company;

    @Column(name = "EmployeeCode", nullable = false, length = 64)
    @Index(name = "ix_employee_code", columnNames = {"code", "name"})
    private String code;

    @Column(name = "EmployeeName", nullable = false, length = 128)
    private String name;

    @Column(name = "updateTimestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp updateTimestamp;

    @Override
    public void updateUpdateTimestamp() {
        updateTimestamp = new Timestamp(new Date().getTime());
    }

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(company, code);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                    .add("id", id)
                    .add("code", code)
                    .add("name", name);
    }
}
