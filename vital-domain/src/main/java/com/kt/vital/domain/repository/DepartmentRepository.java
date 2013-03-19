package com.kt.vital.domain.repository;

import com.kt.vital.domain.model.Department;
import kr.debop4j.data.hibernate.repository.HibernateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * {@link Department} Repository
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 19.
 */
@Repository
@Slf4j
public class DepartmentRepository extends HibernateRepository<Department> {

    public DepartmentRepository() {
        super(Department.class);
    }
}
