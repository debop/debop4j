package com.kt.vital.domain.model.history;

import com.google.common.base.Objects;
import com.kt.vital.domain.model.AlarmType;
import com.kt.vital.domain.model.VitalLogEntityBase;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * 알람 발행 이력
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 18 오후 5:10
 */
@Entity
@Table(name = "AlarmLog")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class AlarmLog extends VitalLogEntityBase {

    private static final long serialVersionUID = 3263493982600804083L;

    protected AlarmLog() {}

    public AlarmLog(String username, String email, AlarmType alarmType, String levelType) {
        Guard.shouldNotBeEmpty(username, "username");
        Guard.shouldNotBeEmpty(email, "email");

        this.username = username;
        this.email = email;
        this.alarmType = alarmType;
        this.levelType = levelType;
    }

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

    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime alarmTime;

    /**
     * TODO: 파악 필요 (의미 ???)
     */
    @Column(nullable = false, length = 512)
    private String topicName;

    /**
     * TODO: 파악 필요 (의미 ???)
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
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime alarmTakenTime;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(getId());
        return HashTool.compute(username, alarmType, levelType);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("username", username)
                .add("alarmType", alarmType)
                .add("levelType", levelType);
    }
}
