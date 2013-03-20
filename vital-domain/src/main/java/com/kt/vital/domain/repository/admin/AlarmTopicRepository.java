package com.kt.vital.domain.repository.admin;

import com.kt.vital.domain.model.admin.AlarmTopic;
import kr.debop4j.data.hibernate.repository.HibernateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * {@link AlarmTopic} Repository
 *
 * @author: sunghyouk.bae@gmail.com
 */
@Repository
@Slf4j
public class AlarmTopicRepository extends HibernateRepository<AlarmTopic> {
    public AlarmTopicRepository() {
        super(AlarmTopic.class);
    }
}
