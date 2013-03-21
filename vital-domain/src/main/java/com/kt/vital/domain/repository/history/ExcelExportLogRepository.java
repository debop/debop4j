package com.kt.vital.domain.repository.history;

import com.kt.vital.domain.model.history.ExcelExportLog;
import kr.debop4j.data.hibernate.repository.HibernateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * {@link com.kt.vital.domain.model.history.ExcelExportLog} Repository
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 19.
 */
@Repository
@Slf4j
public class ExcelExportLogRepository extends HibernateRepository<ExcelExportLog> {

    public ExcelExportLogRepository() {
        super(ExcelExportLog.class);
    }
}
