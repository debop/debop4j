package com.kt.vital.domain.repository;

import com.kt.vital.domain.model.logs.AlarmLog;
import kr.debop4j.data.hibernate.repository.HibernateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * {@link com.kt.vital.domain.model.logs.AlarmLog} Repository
 *
 * @author : sunghyouk.bae@gmail.com
 */
@Repository
@Slf4j
public class AlarmHistoryRepository extends HibernateRepository<AlarmLog> {
    public AlarmHistoryRepository() {
        super(AlarmLog.class);
    }
}
