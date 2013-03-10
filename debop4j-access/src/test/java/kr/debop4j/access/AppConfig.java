package kr.debop4j.access;

import kr.debop4j.access.model.organization.Company;
import kr.debop4j.access.repository.CompanyRepository;
import kr.debop4j.access.service.OrganizationService;
import kr.debop4j.data.hibernate.repository.IHibernateRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * kr.debop4j.access.AppConfig
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 2.
 */
@Configuration
@EnableTransactionManagement
public class AppConfig extends UsingPostgreSqlConfiguration {

    @Bean
    public IHibernateRepository<Company> companyRepository() {
        return new CompanyRepository();
    }

    @Bean
    public OrganizationService organizationService() {
        return new OrganizationService();
    }
}
