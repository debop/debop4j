package kr.debop4j.access.model;

import com.google.common.base.Objects;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;

import javax.persistence.*;

/**
 * kr.debop4j.access.model.Employee
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 1.
 */
@Entity
@Table(name = "Employee")
@Inheritance(strategy = InheritanceType.JOINED)
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Employee extends AccessEntityBase implements ICodeBaseEntity {

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
    @Index(name = "ix_employee_code")
    private Company company;

    @Column(name = "EmployeeCode", nullable = false, length = 64)
    @Index(name = "ix_employee_code")
    private String code;

    @Column(name = "EmployeeName", nullable = false, length = 128)
    @Index(name = "ix_employee_code")
    private String name;

    @Column(name = "IsActive")
    private Boolean active;

    @Column(name = "Description", length = 4000)
    private String description;

    @Column(name = "ExAttr", length = 4000)
    private String exAttr;

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
                    .add("companyId", company.getId())
                    .add("code", code)
                    .add("name", name);
    }
}
