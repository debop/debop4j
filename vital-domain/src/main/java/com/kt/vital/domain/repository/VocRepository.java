package com.kt.vital.domain.repository;

import com.kt.vital.domain.model.Voc;
import kr.debop4j.data.hibernate.repository.HibernateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * {@link com.kt.vital.domain.model.Voc} 의 Repository
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 18.
 */
@Repository
@Slf4j
public class VocRepository extends HibernateRepository<Voc> {

    public VocRepository() {
        super(Voc.class);
    }
}
