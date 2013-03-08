package kr.debop4j.access.model;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import kr.debop4j.core.Guard;
import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.ILocaleValue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.Locale;
import java.util.Map;

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
public class Company extends AccessLocaledEntityBase<Company.CompanyLocale> implements ICodeBaseEntity {

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

    @Column(name = "ExAttr", length = 4000)
    private String exAttr;

    /**
     * 다국어 지원을 위한 정보
     */
    @CollectionTable(name = "CompanyLocale", joinColumns = @JoinColumn(name = "CompanyId"))
    @MapKeyClass(Locale.class)
    @ElementCollection(targetClass = CompanyLocale.class, fetch = FetchType.LAZY)
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
                    .add("name", name);
    }

    @Getter
    @Setter
    @Embeddable
    @DynamicInsert
    @DynamicUpdate
    public static class CompanyLocale extends ValueObjectBase implements ILocaleValue {

        public CompanyLocale() {}

        public CompanyLocale(String name, String description) {
            this.name = name;
            this.description = description;
        }

        private String name;
        private String description;
    }
}
