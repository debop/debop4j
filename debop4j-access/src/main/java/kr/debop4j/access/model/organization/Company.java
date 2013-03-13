package kr.debop4j.access.model.organization;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import kr.debop4j.access.model.AccessLocaledEntityBase;
import kr.debop4j.access.model.ICodeBaseEntity;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.ILocaleValue;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Locale;
import java.util.Map;

/**
 * 회사 정보
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 1.
 */
@Entity
@Table(name = "Company")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Company extends AccessLocaledEntityBase<Company.CompanyLocale> implements ICodeBaseEntity {

    private static final long serialVersionUID = -7337020664879632947L;

    protected Company() {}

    public Company(String companyCode) {
        this(companyCode, companyCode);
    }

    public Company(String companyCode, String companyName) {
        Guard.shouldNotBeEmpty(companyCode, "companyCode");
        Guard.shouldNotBeEmpty(companyName, "companyName");

        this.code = companyCode;
        this.name = companyName;
        this.active = true;
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

    @Basic
    @Column(name = "IsActive")
    private Boolean active;

    @Basic
    @Column(name = "CompanyDesc", length = 2000)
    private String description;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "ExAttr", length = 2000)
    private String exAttr;

    /**
     * 다국어 지원을 위한 정보
     */
    @CollectionTable(name = "CompanyLocale", joinColumns = {@JoinColumn(name = "CompanyId")})
    @ElementCollection(targetClass = CompanyLocale.class, fetch = FetchType.LAZY)
    @MapKeyClass(Locale.class)
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    private Map<Locale, CompanyLocale> localeMap = Maps.newHashMap();

    public Map<Locale, CompanyLocale> getLocaleMap() {
        return localeMap;
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
                .add("name", name)
                .add("active", active)
                .add("description", description);
    }

    @Data
    @Embeddable
    @DynamicInsert
    @DynamicUpdate
    public static class CompanyLocale implements ILocaleValue {

        public CompanyLocale() {}

        public CompanyLocale(String name, String description) {
            this.name = name;
            this.description = description;
        }

        @Column(name = "CompanyName", length = 128)
        private String name;

        @Basic
        @Column(name = "CompanyDesc", length = 2000)
        private String description;
    }
}
