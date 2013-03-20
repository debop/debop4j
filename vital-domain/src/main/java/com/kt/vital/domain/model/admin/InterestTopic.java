package com.kt.vital.domain.model.admin;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 관심 토픽
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 20 오전 11:49
 */
@Entity
@DiscriminatorValue("Interest")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class InterestTopic extends TopicBase {

    private static final long serialVersionUID = -6663782333596118359L;

    protected InterestTopic() {}

    public InterestTopic(String topicName) {
        super(topicName);
    }
}
