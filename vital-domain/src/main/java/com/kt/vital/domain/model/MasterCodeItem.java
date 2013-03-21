package com.kt.vital.domain.model;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

/**
 * 마스터 코드 아이템
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 21 오전 11:13
 */
@Entity
@Table
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class MasterCodeItem extends VitalEntityBase {

    private static final long serialVersionUID = 6092682381610821754L;

    protected MasterCodeItem() {}

    public MasterCodeItem(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public MasterCodeItem(MasterCode masterCode, String name, String value) {
        this(name, value);
        this.masterCode = masterCode;
    }

    @Id
    @GeneratedValue
    @Column(name = "ItemId")
    private Long id;

    /**
     * 마스터 코드
     */
    @ManyToOne
    @JoinColumn(name = "MasterCodeId")
    private MasterCode masterCode;

    /**
     * 마스터 코드의 이름 (ex: 불만 | 단순문의)
     */
    @NotEmpty
    @Column(nullable = false, length = 128)
    private String name;

    /**
     * 마스터 코드의 값 (ex: SR_C | SR_Q)
     */
    @NotEmpty
    @Column(nullable = false, length = 255)
    private String value;

    /**
     * 설명
     */
    @Column(length = 2000)
    private String description;

    /**
     * 부가 특성
     */
    @Column(length = 2000)
    private String exAttr;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(name, value);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("name", name)
                .add("value", value);
    }
}
