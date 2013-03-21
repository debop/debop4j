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
 * 알람 발행 규칙
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 18 오후 4:14
 */
@Entity
@Table(name = "AlarmRule")
@org.hibernate.annotations.Cache(region = "Vital.Common", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class AlarmRule extends VitalEntityBase {

    protected AlarmRule() {}

    public AlarmRule(User user, AlarmType alarmType, String frequencyGroup, String levelType) {
        Guard.shouldNotBeNull(user, "user");
        this.user = user;
        this.alarmType = alarmType;
        this.frequencyGroup = frequencyGroup;
        this.levelType = levelType;
        this.enabled = true;
    }

    @Id
    @GeneratedValue
    @Column(name = "AlarmId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "UserId")
    @Index(name = "ix_alarmpublish_user")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 128)
    private AlarmType alarmType;

    /**
     * 빈도수 그룹 (???)
     */
    private String frequencyGroup;

    /**
     * 레벨 (상/중/하)
     */
    private String levelType;

    /**
     * 기준값 빈도수 하한 (상하한 경계선을 넘어서면 알람)
     */
    private Integer frequencyFrom;

    /**
     * 기준값 빈도수 상한
     */
    private Integer frequencyTo;

    /**
     * 증감율 (이 값 이상이면 알람)
     */
    private Double ratio;

    /**
     * 가능 여부
     */
    private Boolean enabled;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(user, alarmType, frequencyGroup, levelType);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("username", user.getUsername())
                .add("enabled", enabled)
                .add("alarmType", alarmType)
                .add("frequencyGroup", frequencyGroup)
                .add("levelType", levelType);
    }
}
