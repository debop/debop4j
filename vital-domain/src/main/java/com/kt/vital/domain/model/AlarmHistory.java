package com.kt.vital.domain.model;

import com.google.common.base.Objects;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 알람 발행 이력
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 18 오후 5:10
 */
@Entity
@Table(name = "AlarmHistory")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class AlarmHistory extends VitalEntityBase {

    protected AlarmHistory() {}

    public AlarmHistory(String username, String email, AlarmType alarmType, String levelType) {
        Guard.shouldNotBeEmpty(username, "username");
        Guard.shouldNotBeEmpty(email, "email");

        this.username = username;
        this.email = email;
        this.alarmType = alarmType;
        this.levelType = levelType;
    }

    @Id
    @GeneratedValue
    private Long id;

    /**
     * 알람 수신 사용자
     */
    @Column(nullable = false, length = 50)
    private String username;

    @Column(length = 50)
    private String email;

    /**
     * 알람 타입
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 128)
    private AlarmType alarmType;

    /**
     * 레벨 (상/중/하)
     */
    private String levelType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date alarmTime;

    /**
     * ???
     */
    @Column(nullable = false, length = 512)
    private String topicName;

    /**
     * ???
     */
    @Column(nullable = false, length = 100)
    private String dateType;

    /**
     * 알람 발생 시의 빈도수
     */
    private Integer frequency;

    /**
     * 알람 확인 시간
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date alarmTakenTime;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(username, alarmType, levelType);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("username", username)
                .add("alarmType", alarmType)
                .add("levelType", levelType);
    }
}
