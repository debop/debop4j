package com.kt.vital.domain.service;

import com.kt.vital.domain.repository.logs.AlarmLogRepository;
import com.kt.vital.domain.repository.logs.ExcelExportLogRepository;
import com.kt.vital.domain.repository.logs.UserActionLogRepository;
import com.kt.vital.domain.repository.logs.VocImportLogRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 여러가지 로그에 대한 작업을 제공합니다.
 *
 * @author: sunghyouk.bae@gmail.com
 */
@Service
@Slf4j
@Getter
public class LogService extends VitalServiceBase {

    @Autowired
    private AlarmLogRepository alarmLogRepository;

    @Autowired
    private ExcelExportLogRepository excelLogRepository;

    @Autowired
    private UserActionLogRepository userActionLogRepository;

    @Autowired
    private VocImportLogRepository vocImportLogRepository;
}
