package com.kt.vital.domain.repository.admin;

import com.kt.vital.domain.model.admin.InterestTopic;
import kr.debop4j.data.hibernate.repository.HibernateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * @author: sunghyouk.bae@gmail.com
 */
@Repository
@Slf4j
public class InterestTopicRepository extends HibernateRepository<InterestTopic> {

    public InterestTopicRepository() {
        super(InterestTopic.class);
    }
}
