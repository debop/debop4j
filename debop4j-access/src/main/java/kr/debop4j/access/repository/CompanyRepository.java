package kr.debop4j.access.repository;

import kr.debop4j.access.model.organization.Company;
import kr.debop4j.data.hibernate.repository.HibernateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * Company용 Repository
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 1.
 */
@Repository
@Slf4j
public class CompanyRepository extends HibernateRepository<Company> {

    public CompanyRepository() {
        super(Company.class);

        if (log.isDebugEnabled())
            log.debug("CompanyRepository를 생성했습니다.");
    }
}
