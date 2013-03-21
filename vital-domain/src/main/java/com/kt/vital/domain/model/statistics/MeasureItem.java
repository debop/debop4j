package com.kt.vital.domain.model.statistics;

import com.google.common.base.Objects;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * 측정값을 표현합니다. 하나의 측정값입니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@Entity
@Table(name = "MeasureItem")
@org.hibernate.annotations.Table(appliesTo = "MEASURE_ITEM",
                                 indexes = {@Index(name = "ix_measure_item",
                                                   columnNames = {"MeasureId", "MeasureTime"})})
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class MeasureItem extends AnnotatedEntityBase {

    private static final long serialVersionUID = -7295020314225926275L;

    protected MeasureItem() { }

    public MeasureItem(Measure measure, DateTime time) {
        this(measure, time, 0.0);
    }

    public MeasureItem(Measure measure, DateTime time, double value) {
        Guard.shouldNotBeNull(measure, "measure");

        this.measure = measure;
        this.time = time;
        this.value = value;
    }

    @Id
    @GeneratedValue
    @Column(name = "MeasureItemId")
    private Long id;

    /**
     * 측정 항목 정보
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MeasureId")
    private Measure measure;

    /**
     * 측정 시각
     */
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    @Column(name = "MeasureTime")
    private DateTime time;

    /**
     * 측정 값
     */
    @Basic
    @Column(name = "MeasureValue")
    private double value = 0.0;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(measure, time);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("time", time)
                .add("value", value);
    }
}
