package com.kt.vital.domain.repository.history;

import com.kt.vital.domain.model.history.UserActionLog;
import kr.debop4j.data.hibernate.repository.HibernateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * {@link com.kt.vital.domain.model.history.UserActionLog} Repository
 *
 * @author sunghyouk.bae@gmail.com
 */
@Repository
@Slf4j
public class UserActionLogRepository extends HibernateRepository<UserActionLog> {

    public UserActionLogRepository() {
        super(UserActionLog.class);
    }
}
