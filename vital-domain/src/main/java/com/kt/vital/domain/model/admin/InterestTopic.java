package com.kt.vital.domain.model.admin;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 관심 토픽
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 20 오전 11:49
 */
@Entity
@DiscriminatorValue("Interest")
public class InterestTopic extends TopicBase {

    private static final long serialVersionUID = -6663782333596118359L;

    protected InterestTopic() {}

    public InterestTopic(String topicName) {
        super(topicName);
    }
}
