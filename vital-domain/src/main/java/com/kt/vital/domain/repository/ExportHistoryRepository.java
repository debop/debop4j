package com.kt.vital.domain.repository;

import com.kt.vital.domain.model.logs.ExcelExportLog;
import kr.debop4j.data.hibernate.repository.HibernateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * {@link com.kt.vital.domain.model.logs.ExcelExportLog} Repository
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 19.
 */
@Repository
@Slf4j
public class ExportHistoryRepository extends HibernateRepository<ExcelExportLog> {

    public ExportHistoryRepository() {
        super(ExcelExportLog.class);
    }
}
