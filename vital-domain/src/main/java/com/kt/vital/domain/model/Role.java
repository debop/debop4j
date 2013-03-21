package com.kt.vital.domain.model;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;

/**
 * 시스템을 사용하는 사람들에 대한 역할을 표현합니다. - 관리자, 일반 사용자 등
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 18 오후 4:19
 */
@Entity
@Table(name = "Role")
@org.hibernate.annotations.Cache(region = "Vital.Common", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Role extends VitalEntityBase {

    private static final long serialVersionUID = -6008160718626429047L;

    protected Role() {}

    public Role(String code, String name) {
        Guard.shouldNotBeEmpty(code, "code");
        Guard.shouldNotBeEmpty(name, "name");

        this.code = code;
        this.name = name;
        this.enabled = true;
    }

    @Id
    @GeneratedValue
    @Column(name = "RoleId")
    private Long id;

    @Column(name = "RoleCode", nullable = false, length = 50)
    @Index(name = "ix_role_code")
    private String code;

    @Column(name = "RoleName", nullable = false, length = 50)
    @Index(name = "ix_role_code")
    private String name;

    @Basic
    private Boolean enabled;

    @Column(name = "RoleDesc", nullable = false, length = 1024)
    private String description;

    /**
     * 현재 Role이 가지는 권한 정보
     */
    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JoinTable(name = "RoleAuthority",
               joinColumns = {@JoinColumn(name = "RoleId")},
               inverseJoinColumns = {@JoinColumn(name = "AuthId")})
    private Set<Authority> authorities = Sets.newHashSet();

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(name);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("code", code)
                .add("name", name)
                .add("enabled", enabled)
                .add("description", description);
    }
}
