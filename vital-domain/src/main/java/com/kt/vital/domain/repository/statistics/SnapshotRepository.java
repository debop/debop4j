package com.kt.vital.domain.repository.statistics;

import com.kt.vital.domain.model.statistics.Snapshot;
import kr.debop4j.data.hibernate.repository.HibernateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * {@link Snapshot} Repository
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 22 오후 12:19
 */
@Repository
@Slf4j
public class SnapshotRepository extends HibernateRepository<Snapshot> {

    public SnapshotRepository() {
        super(Snapshot.class);
    }
}
