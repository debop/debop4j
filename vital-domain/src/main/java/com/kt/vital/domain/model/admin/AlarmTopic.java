package com.kt.vital.domain.model.admin;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 알람 토픽 - 알람을 보낼 토픽
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 20 오전 11:47
 */
@Entity
@DiscriminatorValue("Alarm")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class AlarmTopic extends TopicBase {

    private static final long serialVersionUID = 2080638127858678530L;

    protected AlarmTopic() {}

    public AlarmTopic(String topicName) {
        super(topicName);
    }
}
