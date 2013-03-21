package com.kt.vital.domain.model;

import com.google.common.base.Objects;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;

import javax.persistence.*;

/**
 * 권한을 나타냅니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 18 오후 4:21
 */
@Entity
@org.hibernate.annotations.Cache(region = "Vital.Common", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Authority extends VitalEntityBase {

    protected Authority() {}

    public Authority(String name, String description) {
        Guard.shouldNotBeEmpty(name, "name");

        this.name = name;
        this.description = description;
        this.enabled = true;
    }

    @Id
    @GeneratedValue
    @Column(name = "AuthId")
    private Long id;

    /**
     * 권한명 : SR-Report, EXCEL-Download 등
     */
    @Column(name = "AuthName", nullable = false, length = 128)
    @Index(name = "ix_authority_name")
    private String name;

    /**
     * 권한에 대한 설명 : SR 정보를 리포팅합니다, 엑셀 다운로드를 할 수 있습니다.
     */
    @Column(name = "AuthDesc", length = 2000)
    private String description;

    @Basic
    private Boolean enabled;

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
                .add("name", name)
                .add("enabled", enabled)
                .add("desc", description);
    }
}
