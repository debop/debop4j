package com.kt.vital.domain.repository;

import com.kt.vital.domain.model.VoC;
import kr.debop4j.data.hibernate.repository.HibernateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * com.kt.vital.domain.repository.VoCRepository
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 18.
 */
@Repository
@Slf4j
public class VoCRepository extends HibernateRepository<VoC> {

    public VoCRepository() {
        super(VoC.class);
    }
}
