package com.kt.vital.domain.service;

import com.kt.vital.domain.repository.VocRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Voc 관련 서비스
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 19 오후 2:43
 */
@Service
@Slf4j
public class VoCService extends VitalServiceBase {

    @Autowired
    @Getter
    private VocRepository vocRepository;
}
