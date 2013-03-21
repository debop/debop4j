package com.kt.vital.domain.model.statistics;

import com.google.common.base.Objects;
import com.kt.vital.domain.model.VitalEntityBase;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 측정 항목에 대한 설명
 *
 * @author sunghyouk.bae@gmail.com
 */
@Entity
@Table(name = "Measure")
@Cache(region = "Vital.Stats", usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Measure extends VitalEntityBase {

    private static final long serialVersionUID = 7701909790216578174L;

    protected Measure() {}

    public Measure(String name) {
        this(name, "");
    }

    public Measure(String name, String unit) {
        this.name = name;
        this.unit = unit;
    }

    @Id
    @GeneratedValue
    @Column(name = "MeasureId")
    private Long id;

    /**
     * 측정 명 (총 VoC Count)
     */
    @NotEmpty
    @Column(name = "MeasureName", nullable = false, length = 128)
    @Index(name = "ix_measure_name")
    private String name;

    /**
     * 측정 값의 단위 (건수 등)
     */
    @Column(name = "MeasureUnit", length = 128)
    private String unit;

    /**
     * 측정 항목에 대한 설명
     */
    @Column(name = "MeasureDesc", length = 2000)
    private String description;

    @Column(length = 2000)
    private String exAttr;

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
                .add("unit", unit);
    }
}
