package com.kt.vital.domain.repository;

import com.kt.vital.domain.model.logs.UserActionLog;
import kr.debop4j.data.hibernate.repository.HibernateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * {@link com.kt.vital.domain.model.logs.UserActionLog} Repository
 *
 * @author sunghyouk.bae@gmail.com
 */
@Repository
@Slf4j
public class UserActionHistoryRepository extends HibernateRepository<UserActionLog> {

    public UserActionHistoryRepository() {
        super(UserActionLog.class);
    }
}
