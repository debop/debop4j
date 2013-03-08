package kr.debop4j.access.model.common;

import com.google.common.base.Objects;
import kr.debop4j.access.model.AccessEntityBase;
import kr.debop4j.access.model.Company;
import kr.debop4j.access.model.ICodeBaseEntity;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;

import javax.persistence.*;

/**
 * 직급, 직위, 직책 등 직원과 관련된 코드 정보를 나타내는 추상클래스입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 8 오후 4:45
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public abstract class EmployeeCodeBase extends AccessEntityBase implements ICodeBaseEntity {

    protected EmployeeCodeBase() {}

    protected EmployeeCodeBase(Company company, String code) {
        this(company, code, code);
    }

    protected EmployeeCodeBase(Company company, String code, String name) {
        Guard.shouldNotBeNull(company, "company");
        Guard.shouldNotBeEmpty(code, "code");
        Guard.shouldNotBeEmpty(name, "name");

        this.company = company;
        this.code = code;
        this.name = name;
    }

    @Id
    @GeneratedValue
    @Column(name = "CodeId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CompanyId")
    @Index(name = "ix_employeecode")
    private Company company;

    @Column(name = "CodeValue", nullable = false, length = 128)
    @Index(name = "ix_employeecode")
    private String code;

    @Column(name = "CodeName", nullable = false, length = 256)
    @Index(name = "ix_employeecode")
    private String name;

    @Column(name = "ViewOrder")
    private Integer viewOrder;

    @Column(name = "Description", length = 4000)
    private String description;

    @Column(name = "ExAttr", length = 2000)
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
                    .add("code", code)
                    .add("name", name)
                    .add("company", company);
    }
}
