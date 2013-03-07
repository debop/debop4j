package kr.debop4j.access;

import kr.debop4j.access.model.Company;
import kr.debop4j.data.hibernate.springconfiguration.PostgreSqlConfigBase;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

/**
 * kr.debop4j.access.UsingPostgreSqlConfiguration
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 7.
 */
@Configuration
@EnableTransactionManagement
public class UsingPostgreSqlConfiguration extends PostgreSqlConfigBase {

    @Override
    public String getDatabaseName() {
        return "HAccess";
    }

    @Override
    protected String[] getMappedPackageNames() {
        return new String[]{Company.class.getPackage().getName()};
    }

    @Override
    public Properties hibernateProperties() {
        return super.hibernateProperties();
    }
}
