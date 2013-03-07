package kr.debop4j.access.model;

import com.google.common.base.Objects;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedEntityBase;
import kr.debop4j.data.model.IUpdateTimestampedEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.Date;

/**
 * 회사 정보
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 1.
 */
@Entity
@Table(name = "Company")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Company extends AnnotatedEntityBase implements IUpdateTimestampedEntity {

    protected Company() {}

    public Company(String companyCode) {
        this(companyCode, companyCode);
    }

    public Company(String companyCode, String companyName) {
        Guard.shouldNotBeEmpty(companyCode, "companyCode");
        Guard.shouldNotBeEmpty(companyName, "companyName");

        this.code = companyCode;
        this.name = companyName;
    }

    @Id
    @GeneratedValue
    @Column(name = "CompanyId")
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @Column(name = "CompanyCode", nullable = false, length = 128)
    @Index(name = "ix_company_code")
    private String code;

    @Column(name = "CompanyName", nullable = false, length = 128)
    @Index(name = "ix_company_code")
    private String name;

    @Column(name = "CompanyEName", length = 128)
    private String ename;

    @Column(name = "IsActive")
    private Boolean active;

    @Column(name = "CompanyDesc", length = 4000)
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTimestamp;

    @Override
    public void updateUpdateTimestamp() {
        updateTimestamp = new Date();
    }

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(code);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("code", code)
                .add("name", name);
    }
}
