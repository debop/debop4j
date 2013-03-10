package kr.debop4j.access.model.common;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import kr.debop4j.access.model.AccessEntityBase;
import kr.debop4j.access.model.organization.Company;
import kr.debop4j.core.tools.HashTool;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;

/**
 * 코드
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 8 오후 1:07
 */
@Entity
@Table(name = "`Code`")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Code extends AccessEntityBase {

    private Code() {}

    public Code(Company company, String code, String name) {
        this.company = company;
        this.code = code;
        this.name = name;
    }

    @Id
    @GeneratedValue
    @Column(name = "CodeId")
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CompanyId", nullable = false)
    @Index(name = "ix_code")
    private Company company;

    @Column(name = "CodeValue", nullable = false, length = 128)
    @Index(name = "ix_code")
    private String code;

    @Column(name = "CodeName", nullable = false, length = 255)
    @Index(name = "ix_code")
    private String name;

    @OneToMany(mappedBy = "code", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @LazyCollection(value = LazyCollectionOption.EXTRA)
    private Set<CodeItem> items = Sets.newHashSet();

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
                .add("companyId", (company != null) ? company.getId() : null)
                .add("code", code)
                .add("name", name);
    }
}
