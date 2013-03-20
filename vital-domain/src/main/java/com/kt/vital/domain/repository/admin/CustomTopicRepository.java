package com.kt.vital.domain.repository.admin;

import com.kt.vital.domain.model.admin.CustomTopic;
import kr.debop4j.data.hibernate.repository.HibernateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * com.kt.vital.domain.repository.admin.CustomTopicRepository
 *
 * @author: sunghyouk.bae@gmail.com
 */
@Repository
@Slf4j
public class CustomTopicRepository extends HibernateRepository<CustomTopic> {

    public CustomTopicRepository() {
        super(CustomTopic.class);
    }
}
