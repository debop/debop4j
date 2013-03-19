package com.kt.vital.domain.repository;

import com.kt.vital.domain.model.AlarmHistory;
import kr.debop4j.data.hibernate.repository.HibernateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * {@link AlarmHistory} Repository
 *
 * @author : sunghyouk.bae@gmail.com
 */
@Repository
@Slf4j
public class AlarmHistoryRepository extends HibernateRepository<AlarmHistory> {
    public AlarmHistoryRepository() {
        super(AlarmHistory.class);
    }
}
