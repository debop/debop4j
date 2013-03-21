package com.kt.vital.domain.model.statistics;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.joda.time.DateTime;

import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.Set;

/**
 * 실시간 통계 데이터의 각 시간대별 기록
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 21 오후 12:01
 */
@Entity
@Table(name = "RealtimeSnapshot")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class RealtimeSnapshot extends StatisticsDataBase {

    private static final long serialVersionUID = 7631944375999540754L;

    public RealtimeSnapshot() {
        this(new Date());
    }

    public RealtimeSnapshot(Date createTime) {
        this.createDate = new DateTime(createTime.getTime()).withTimeAtStartOfDay().toDate();
        this.createTime = createTime;
    }

    @Id
    @GeneratedValue
    @Column(name = "RealtimeId")
    private Long id;

    /**
     * Shapshot name
     */
    @Column(name = "SnapshotName", nullable = false, length = 255)
    private String name;

    /**
     * Snapshot 일자 (따로 둔 것은 검색 시 편하게 하기 위해)
     */
    @Temporal(TemporalType.DATE)
    private Date createDate;

    /**
     * Snapshot 시각
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @LazyCollection(value = LazyCollectionOption.EXTRA)
    @Fetch(FetchMode.JOIN)
    private Set<StatisticValue> values = Sets.newHashSet();

    public Double getValueByName(String valueName) {
        for (StatisticValue sv : values) {
            if (sv.getName().equals(name))
                return sv.getValue();
        }
        return null;
    }
}
