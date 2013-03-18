package com.kt.vital.domain.model.organization;

import com.google.common.base.Objects;
import com.kt.vital.domain.model.VitalEntityBase;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;

import javax.persistence.*;

/**
 * 시스템을 사용하는 사람들에 대한 역할을 표현합니다. - 관리자, 일반 사용자 등
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 18 오후 4:19
 */
@Entity
@Table(name = "Role")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Role extends VitalEntityBase {

    protected Role() {}

    public Role(String code, String name) {
        Guard.shouldNotBeEmpty(code, "code");
        Guard.shouldNotBeEmpty(name, "name");

        this.code = code;
        this.name = name;
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

    @Column(name = "RoleDesc", nullable = false, length = 1024)
    private String description;


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
                    .add("description", description);
    }
}
