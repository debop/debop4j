package kr.debop4j.access.model;

import com.google.common.base.Objects;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.IUpdateTimestampedEntity;
import kr.debop4j.data.model.TreeEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 부서 정보를 나타냅니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 1.
 */
@Entity
@Table
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Department extends TreeEntityBase<Department, Long> implements IUpdateTimestampedEntity, ICodeBaseEntity {

    private static final long serialVersionUID = -2198558891376603272L;

    protected Department() {}

    public Department(Company company, String departmentCode) {
        this(company, departmentCode, departmentCode);
    }

    public Department(Company company, String departmentCode, String departmentName) {
        Guard.shouldNotBeNull(company, "company");
        Guard.shouldNotBeEmpty(departmentCode, "departmentCode");
        Guard.shouldNotBeEmpty(departmentName, "departmentName");

        this.company = company;
        this.code = departmentCode;
        this.name = departmentName;
    }

    @Id
    @GeneratedValue
    @Column(name = "DepartmentId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CompanyId", nullable = false)
    private Company company;

    @Column(name = "DepartmentCode", nullable = false, length = 64)
    @Index(name = "ix_department_code", columnNames = {"code", "name"})
    private String code;

    @Column(name = "DepartmentName", nullable = false, length = 128)
    private String name;

    @Column(name = "DepartmentEName", length = 128)
    private String enam;

    @Column(name = "DepartmentDesc", length = 4000)
    private String description;

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
