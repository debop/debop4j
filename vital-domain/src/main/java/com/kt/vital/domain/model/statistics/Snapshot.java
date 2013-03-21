package com.kt.vital.domain.model.statistics;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Set;

/**
 * 측정 데이터의 스냅샷 정보 - 주기적으로 여러 측정값을 측정했을 시에 저장하기 좋게 한다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@Entity
@Table(name = "Snapshot")
@org.hibernate.annotations.Cache(region = "Vital.Stats", usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Snapshot extends StatisticEntityBase {

    private static final long serialVersionUID = 7631944375999540754L;

    public Snapshot() {
        this(DateTime.now());
    }

    public Snapshot(DateTime snapshotTime) {
        this.snapshotTime = snapshotTime;
    }

    @Id
    @GeneratedValue
    @Column(name = "SnapshotId")
    private Long id;

    /**
     * Shapshot name
     */
    @Column(name = "SnapshotName", nullable = false, length = 255)
    private String name;

    /**
     * Snapshot 생성 시각
     */
    // @Temporal(TemporalType.TIMESTAMP)
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    @Column(name = "SnapshotTime")
    private DateTime snapshotTime;

    /**
     * Snapshot 생성 일자 (Time part 는 제외)
     *
     * @return 스냅샷을 한 시각
     */
    @Transient
    private DateTime getCreatedDate() {
        return snapshotTime.withTimeAtStartOfDay();
    }

    /**
     * 실시간 측정 값들
     */
    @CollectionTable(name = "SnapshotItem", joinColumns = @JoinColumn(name = "SnapshotId"))
    @ElementCollection(targetClass = SnapshotItem.class, fetch = FetchType.EAGER)
    @OrderColumn(name = "ItemName")
    private Set<SnapshotItem> items = Sets.newHashSet();

    public SnapshotItem getDataByName(String valueName) {
        for (SnapshotItem sv : items) {
            if (sv.getName().equals(name))
                return sv;
        }
        return null;
    }
}
