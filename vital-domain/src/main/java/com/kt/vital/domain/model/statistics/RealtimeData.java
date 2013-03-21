package com.kt.vital.domain.model.statistics;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * 실시간 통계 데이터의 스냅샷 정보 - 주기적인 측정 값을 저장한다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 21 오후 12:01
 */
@Entity
@Table(name = "RealtimeData")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class RealtimeData extends StatisticEntityBase {

    private static final long serialVersionUID = 7631944375999540754L;

    public RealtimeData() {
        this(new Date());
    }

    public RealtimeData(Date createdTime) {
        this.createdTime = new DateTime(createdTime);
    }

    @Id
    @GeneratedValue
    @Column(name = "DataId")
    private Long id;

    /**
     * Shapshot name
     */
    @Column(name = "DataName", nullable = false, length = 255)
    private String name;

    /**
     * Snapshot 생성 시각
     */
    // @Temporal(TemporalType.TIMESTAMP)
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime createdTime;

    /**
     * Snapshot 생성 일자 (Time part 는 제외)
     *
     * @return
     */
    @Transient
    private DateTime getCreatedDate() {
        return createdTime.withTimeAtStartOfDay();
    }

    /**
     * 실시간 측정 값들
     */
    @CollectionTable(name = "RealtimeItem", joinColumns = @JoinColumn(name = "DataId"))
    @ElementCollection(targetClass = RealtimeItem.class, fetch = FetchType.EAGER)
    @OrderColumn(name = "ItemName")
    private Set<RealtimeItem> items = Sets.newHashSet();

    public RealtimeItem getDataByName(String valueName) {
        for (RealtimeItem sv : items) {
            if (sv.getName().equals(name))
                return sv;
        }
        return null;
    }
}
