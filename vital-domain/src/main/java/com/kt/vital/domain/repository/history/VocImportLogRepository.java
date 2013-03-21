package com.kt.vital.domain.repository.history;

import com.kt.vital.domain.model.history.VocImportLog;
import kr.debop4j.data.hibernate.repository.HibernateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * com.kt.vital.domain.repository.VocImportLog
 *
 * @author: sunghyouk.bae@gmail.com
 */
@Repository
@Slf4j
public class VocImportLogRepository extends HibernateRepository<VocImportLog> {

    public VocImportLogRepository() {
        super(VocImportLog.class);
    }
}
