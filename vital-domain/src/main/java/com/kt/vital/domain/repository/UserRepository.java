package com.kt.vital.domain.repository;

import com.kt.vital.domain.model.User;
import kr.debop4j.data.hibernate.repository.HibernateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * com.kt.vital.domain.repository.UserRepository
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 19.
 */
@Repository
@Slf4j
public class UserRepository extends HibernateRepository<User> {

    public UserRepository() {
        super(User.class);
    }
}
