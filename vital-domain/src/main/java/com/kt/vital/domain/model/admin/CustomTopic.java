package com.kt.vital.domain.model.admin;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 사용자 정의 토픽
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 20 오전 11:27
 */
@Entity
@DiscriminatorValue("Custom")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class CustomTopic extends TopicBase {

    private static final long serialVersionUID = 8268268985399798095L;

    protected CustomTopic() {}

    public CustomTopic(String topicName) {
        super(topicName);
    }
}
