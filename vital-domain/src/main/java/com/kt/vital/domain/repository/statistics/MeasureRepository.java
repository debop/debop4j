package com.kt.vital.domain.repository.statistics;

import com.kt.vital.domain.model.statistics.Measure;
import kr.debop4j.data.hibernate.repository.HibernateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * {@link Measure} Repository
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 22 오후 12:18
 */
@Repository
@Slf4j
public class MeasureRepository extends HibernateRepository<Measure> {

    public MeasureRepository() {
        super(Measure.class);
    }
}
