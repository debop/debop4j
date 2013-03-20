package com.kt.vital.domain.service;

import com.kt.vital.domain.repository.DepartmentRepository;
import com.kt.vital.domain.repository.EmployeeRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 조직정보 (직원, 부서)와 관련된 서비스입니다.
 *
 * @author: sunghyouk.bae@gmail.com
 */
@Service
@Slf4j
@Getter
public class OrganizationService extends VitalServiceBase {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

}
