package com.kt.vital.domain.repository;

import com.kt.vital.domain.model.UserActionHistory;
import kr.debop4j.data.hibernate.repository.HibernateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * {@link UserActionHistory} Repository
 *
 * @author sunghyouk.bae@gmail.com
 */
@Repository
@Slf4j
public class UserActionHistoryRepository extends HibernateRepository<UserActionHistory> {

    public UserActionHistoryRepository() {
        super(UserActionHistory.class);
    }
}
