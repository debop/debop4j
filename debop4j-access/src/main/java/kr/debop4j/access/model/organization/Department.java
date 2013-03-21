package kr.debop4j.access.model.organization;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import kr.debop4j.access.model.IActor;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedTreeEntityBase;
import kr.debop4j.data.model.IUpdateTimestampedEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.Set;

/**
 * 부서 정보를 나타냅니다. (상위부서, 하위부서 등을 표현합니다)
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 1.
 */
@Entity
@Table(name = "Department")
@Cache(region = "Organization", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.hibernate.annotations.Table(appliesTo = "Department",
                                 indexes = @org.hibernate.annotations.Index(name = "ix_department_code",
                                                                            columnNames = {"CompanyId", "DepartmentCode"}))
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Department extends AnnotatedTreeEntityBase<Department> implements IActor, IUpdateTimestampedEntity {

    private static final long serialVersionUID = 512869366829603899L;

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
    private String code;

    @Column(name = "DepartmentName", nullable = false, length = 128)
    private String name;

    @Column(name = "DepartmentEName", length = 128)
    private String enam;

    @Column(name = "IsActive")
    private Boolean active;

    @Column(name = "DepartmentDesc", length = 4000)
    private String description;

    @Column(name = "ExAttr", length = 4000)
    private String exAttr;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTimestamp;

    @OneToMany(mappedBy = "department", cascade = {CascadeType.ALL})
    @LazyCollection(value = LazyCollectionOption.EXTRA)
    @Fetch(FetchMode.SELECT)
    private Set<DepartmentMember> members = Sets.newHashSet();

    @Override
    public void updateUpdateTimestamp() {
        updateTimestamp = new Date();
    }

    @Override
    public int hashCode() {
        if (isPersisted())
            return super.hashCode();
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
