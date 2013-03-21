package com.kt.vital.domain.model.statistics;

import com.google.common.base.Objects;
import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Basic;
import javax.persistence.Embeddable;

/**
 * 통계 데이타 하나의 값
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 21 오후 12:08
 */
@Embeddable
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class StatisticValue extends ValueObjectBase {

    private static final long serialVersionUID = 2211422186112407893L;

    protected StatisticValue() {}

    public StatisticValue(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public StatisticValue(String name, double value, String unit) {
        this(name, value);
        this.unit = unit;
    }

    /**
     * 통계 데이터 명
     */
    @NotEmpty
    private String name;

    /**
     * 통계 데이터 값
     */
    @Basic
    public double value = 0.0;

    /**
     * 통계 데이터 단위
     */
    public String unit;

    @Override
    public int hashCode() {
        return HashTool.compute(name, value, unit);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                    .add("name", name)
                    .add("value", value)
                    .add("unit", unit);
    }

}
