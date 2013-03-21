package com.kt.vital.domain.model.statistics;

import com.google.common.base.Objects;
import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.core.tools.HashTool;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 * 스냅샷 한 데이타 하나의 값
 *
 * @author sunghyouk.bae@gmail.com
 *         Date: 13. 3. 21 오후 12:08
 */
@Embeddable
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class SnapshotItem extends ValueObjectBase {

    private static final long serialVersionUID = 2211422186112407893L;

    protected SnapshotItem() {}

    public SnapshotItem(String name, double value) {
        this(name, value, "");
    }

    public SnapshotItem(String name, double value, String unit) {
        this.name = name;
        this.value = value;
        this.unit = unit;
    }

    /**
     * 통계 데이터 명
     */
    @NotEmpty
    @Column(name = "ItemName", length = 128)
    private String name;

    /**
     * 통계 데이터 값
     */
    @Basic
    @Column(name = "ItemValue")
    public double value = 0.0;

    /**
     * 통계 데이터 단위
     */
    @Column(name = "ItemUnit", length = 128)
    public String unit;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PRIVATE)
    @Transient
    private Integer hash;

    @Override
    public int hashCode() {
        if (hash == null)
            hash = HashTool.compute(name, value, unit);
        return hash;
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("name", name)
                .add("value", value)
                .add("unit", unit);
    }
}
