package com.kt.vital.domain.repository;

import com.kt.vital.domain.model.logs.VocImportLog;
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
public class VocImportHistoryRepository extends HibernateRepository<VocImportLog> {

    public VocImportHistoryRepository() {
        super(VocImportLog.class);
    }
}
