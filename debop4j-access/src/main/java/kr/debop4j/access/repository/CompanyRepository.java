package kr.debop4j.access.repository;

import kr.debop4j.access.model.Company;
import kr.debop4j.data.hibernate.repository.HibernateRepository;
import org.springframework.stereotype.Repository;

/**
 * Company용 Repository
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 1.
 */
@Repository
public class CompanyRepository extends HibernateRepository<Company> {

    public CompanyRepository() {
        super(Company.class);
    }
}
